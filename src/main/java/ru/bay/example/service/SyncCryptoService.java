package ru.bay.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.CryptoPro.AdES.Options;
import ru.CryptoPro.CAdES.CAdESSignature;
import ru.CryptoPro.CAdES.CAdESType;
import ru.CryptoPro.CAdES.exception.CAdESException;
import ru.CryptoPro.JCP.tools.Encoder;
import ru.bay.example.config.property.CryptoProperties;
import ru.bay.example.exception.CAdESSignatureException;
import ru.bay.example.exception.KeyStoreOperationException;
import ru.bay.example.exception.SigningOperationException;
import ru.bay.example.model.KeyStoreStatus;
import ru.bay.example.model.KeyStoreStatusResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Deprecated(forRemoval = true)
public class SyncCryptoService {
    private static final String PROVIDER_NAME = "JCSP";
    private static final String ERR_ALIAS_NOT_FOUND = "The alias not found.";
    private static final String ERR_CREDENTIALS_NOT_AVAILABLE = "The credentials is not available.";

    private final KeyStore pfxKeyStore;
    private final CryptoProperties properties;

    public String sign(byte[] data) {
        final KeyStoreStatusResult result = validateStore(pfxKeyStore);
        if (!result.status().equals(KeyStoreStatus.OK)) {
            return result.message();
        }
        var privateKey = getKey(pfxKeyStore);
        var certificate = getCertificate(pfxKeyStore);
        var bytes = doubleSign(data, privateKey, certificate);
        return new Encoder().encode(bytes);
    }

    public KeyStoreStatusResult validateStore(KeyStore store) {
        try {
            var actualAlias = store.aliases().nextElement();
            if (!properties.alias().equals(actualAlias)) {
                return KeyStoreStatusResult.error(ERR_ALIAS_NOT_FOUND);
            }
            var privateKey = getKey(store);
            var certificate = getCertificate(store);
            if (Objects.isNull(privateKey) || Objects.isNull(certificate)) {
                return KeyStoreStatusResult.error(ERR_CREDENTIALS_NOT_AVAILABLE);
            }
            return KeyStoreStatusResult.ok();
        } catch (KeyStoreException ex) {
            return KeyStoreStatusResult.error(ex.getMessage());
        }
    }

    private byte[] doubleSign(byte[] data, PrivateKey privateKey, X509Certificate certificate) {
        var tempBytes = applySignature(data, newCAdESSignatureInstance(privateKey, certificate));
        return applySignature(tempBytes, newCAdESSignatureInstance(privateKey, certificate));
    }

    @SuppressWarnings("java:S3252")
    private CAdESSignature newCAdESSignatureInstance(PrivateKey privateKey, X509Certificate certificate) {
        try {
            var signature = new CAdESSignature();
            signature.setOptions(new Options().disableCertificateValidation());
            // @formatter:off
            signature.addSigner(
                    PROVIDER_NAME,
                    null,
                    null,
                    privateKey,
                    List.of(certificate),
                    CAdESType.CAdES_BES,
                    null,
                    false,
                    null,
                    null,
                    null,
                    true);
            // @formatter:on
            return signature;
        } catch (CAdESException ex) {
            throw new CAdESSignatureException(ex.getMessage());
        }
    }

    private byte[] applySignature(byte[] data, CAdESSignature signature) {
        try (var outputStream = new ByteArrayOutputStream()) {
            signature.open(outputStream);
            signature.update(data);
            signature.close();
            return outputStream.toByteArray();
        } catch (CAdESException | IOException ex) {
            throw new SigningOperationException(ex.getMessage());
        }
    }

    private PrivateKey getKey(KeyStore store) {
        try {
            return (PrivateKey) store.getKey(properties.alias(), properties.password().toCharArray());
        } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException ex) {
            throw new KeyStoreOperationException(ex.getMessage());
        }
    }

    private X509Certificate getCertificate(KeyStore store) {
        try {
            return (X509Certificate) store.getCertificate(properties.alias());
        } catch (KeyStoreException ex) {
            throw new KeyStoreOperationException(ex.getMessage());
        }
    }
}
