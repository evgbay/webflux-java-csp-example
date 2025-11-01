package ru.bay.example.service.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.bay.example.config.property.CryptoProperties;
import ru.bay.example.exception.KeyStoreOperationException;
import ru.bay.example.model.KeyStoreStatus;
import ru.bay.example.model.KeyStoreStatusResult;

import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyStoreProvider {
    private static final String ERR_UNEXPECTED = "Unexpected error during alias validation";
    private static final String ERR_ALIAS_NOT_FOUND = "The alias not found";
    private static final String ERR_ALIAS_NOT_CONTAIN_OBJECT = "Alias does not contain a %s";
    private static final String KEY_WORD = "key";
    private static final String CERTIFICATE_WORD = "certificate";

    private final KeyStore pfxKeyStore;
    private final CryptoProperties properties;

    public Mono<KeyStoreStatusResult> validateKeyStore() {
        return validateAliasPresence()
                .flatMap(result ->
                        (result.status() == KeyStoreStatus.OK)
                                ? validateCredentialsPresence()
                                : Mono.just(result))
                .onErrorResume(ex -> {
                    log.error(ERR_UNEXPECTED, ex);
                    return Mono.just(KeyStoreStatusResult.error(ex.getMessage()));
                });
    }

    public Mono<KeyStoreStatusResult> validateAliasPresence() {
        return Mono.fromCallable(() -> {
            var aliases = pfxKeyStore.aliases();
            while (aliases.hasMoreElements()) {
                if (properties.alias().equals(aliases.nextElement())) {
                    return KeyStoreStatusResult.ok();
                }
            }
            return KeyStoreStatusResult.error(ERR_ALIAS_NOT_FOUND);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<KeyStoreStatusResult> validateCredentialsPresence() {
        var key = getKey();
        var certificate = getCertificate();
        return Mono.zip(key, certificate)
                .map(tuple -> KeyStoreStatusResult.ok());
    }

    public Mono<PrivateKey> getKey() {
        return Mono.fromCallable(() -> {
            Key key = pfxKeyStore.getKey(properties.alias(), properties.password().toCharArray());
            if (key instanceof PrivateKey privateKey) {
                return privateKey;
            }
            throw new KeyStoreOperationException(String.format(ERR_ALIAS_NOT_CONTAIN_OBJECT, KEY_WORD));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<X509Certificate> getCertificate() {
        return Mono.fromCallable(() -> {
            Certificate certificate = pfxKeyStore.getCertificate(properties.alias());
            if (certificate instanceof X509Certificate x509Certificate) {
                return x509Certificate;
            }
            throw new KeyStoreOperationException(String.format(ERR_ALIAS_NOT_CONTAIN_OBJECT, CERTIFICATE_WORD));
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
