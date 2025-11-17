package ru.bay.example.service.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.bay.example.config.property.CryptoProperties;
import ru.bay.example.model.Credentials;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CredentialProvider {

    private final KeyStore pfxKeyStore;
    private final CryptoProperties properties;

    public Mono<Credentials> assembleCredentials() {
        return Mono.zip(getKey(), getCertificate())
                .map(Credentials::from);
    }

    public Mono<PrivateKey> getKey() {
        return Mono.fromCallable(() -> (PrivateKey) pfxKeyStore.getKey(properties.alias(), properties.password().toCharArray()));
    }

    public Mono<X509Certificate> getCertificate() {
        return Mono.fromCallable(() -> (X509Certificate) pfxKeyStore.getCertificate(properties.alias()));
    }
}
