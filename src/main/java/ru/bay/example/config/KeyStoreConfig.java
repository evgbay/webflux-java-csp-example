package ru.bay.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.CryptoPro.JCP.Util.JCPInit;
import ru.bay.example.config.property.CryptoProperties;
import ru.bay.example.exception.KeyStoreOperationException;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
@RequiredArgsConstructor
public class KeyStoreConfig {
    private static final String ERR_UNEXPECTED = "Unexpected exception during KeyStore validation - %s";
    private static final String ERR_ALIAS_NOT_FOUND = "The alias - %s, not found";
    private static final String ERR_ALIAS_NOT_CONTAIN_CREDENTIAL = "Alias does not contain a %s";

    static {
        JCPInit.initProviders(true);
    }

    private final CryptoProperties properties;

    @Bean
    public KeyStore pfxKeyStore() {
        try {
            var store = KeyStore.getInstance(properties.type());
            try (var stream = getClass().getResourceAsStream(properties.path())) {
                store.load(stream, properties.password().toCharArray());
            }
            validateKeyStore(store);
            return store;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            throw new KeyStoreOperationException(ex.getMessage());
        }
    }

    private void validateKeyStore(KeyStore store) {
        validateAliasPresence(store);
        validatePrivateKeyPresence(store);
        validateX509CertificatePresence(store);
    }

    private void validateAliasPresence(KeyStore store) {
        try {
            if (!store.containsAlias(properties.alias())) {
                throw new KeyStoreOperationException(String.format(ERR_ALIAS_NOT_FOUND, properties.alias()));
            }
        } catch (KeyStoreException e) {
            throw new KeyStoreOperationException(String.format(ERR_UNEXPECTED, e.getMessage()));
        }
    }

    private void validatePrivateKeyPresence(KeyStore store) {
        try {
            Key key = store.getKey(properties.alias(), properties.password().toCharArray());
            if (!(key instanceof PrivateKey)) {
                throw new KeyStoreOperationException(String.format(ERR_ALIAS_NOT_CONTAIN_CREDENTIAL, "key"));
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new KeyStoreOperationException(String.format(ERR_UNEXPECTED, e.getMessage()));
        }
    }

    private void validateX509CertificatePresence(KeyStore store) {
        try {
            Certificate certificate = store.getCertificate(properties.alias());
            if (!(certificate instanceof X509Certificate)) {
                throw new KeyStoreOperationException(String.format(ERR_ALIAS_NOT_CONTAIN_CREDENTIAL, "certificate"));
            }
        } catch (KeyStoreException e) {
            throw new KeyStoreOperationException(String.format(ERR_UNEXPECTED, e.getMessage()));
        }
    }
}
