package ru.bay.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.CryptoPro.JCP.Util.JCPInit;
import ru.bay.example.config.property.CryptoProperties;
import ru.bay.example.exception.KeyStoreOperationException;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@RequiredArgsConstructor
public class CryptoConfig {
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
            return store;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException ex) {
            throw new KeyStoreOperationException(ex.getMessage());
        }
    }
}
