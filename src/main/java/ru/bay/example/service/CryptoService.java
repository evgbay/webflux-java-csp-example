package ru.bay.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.CryptoPro.JCP.tools.Encoder;
import ru.bay.example.model.Credentials;
import ru.bay.example.model.KeyStoreStatus;
import ru.bay.example.service.provider.CAdESSignatureProvider;
import ru.bay.example.service.provider.KeyStoreProvider;

import java.io.ByteArrayOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoService {
    private static final String ERR_UNEXPECTED = "Unexpected error during sign operation";

    private final KeyStoreProvider keyStoreProvider;
    private final CAdESSignatureProvider cAdESSignatureProvider;

    public Mono<String> sign(byte[] data) {
        return keyStoreProvider.validateKeyStore()
                .flatMap(result -> {
                    if (result.status() == KeyStoreStatus.OK) {
                        return doubleSign(data)
                                .flatMap(this::encode);
                    }
                    return Mono.just(result.message());
                }).onErrorResume(ex -> {
                    log.error(ERR_UNEXPECTED, ex);
                    return Mono.just(ex.getMessage());
                });
    }

    public Mono<byte[]> doubleSign(byte[] data) {
        var key = keyStoreProvider.getKey();
        var certificate = keyStoreProvider.getCertificate();
        return Mono.zip(key, certificate)
                .map(tuple -> Credentials.from(tuple.getT1(), tuple.getT2()))
                .flatMap(credentials -> applySignature(data, credentials)
                        .flatMap(tempBytes -> applySignature(tempBytes, credentials)));
    }

    public Mono<byte[]> applySignature(byte[] data, Credentials credentials) {
        return cAdESSignatureProvider.newInstance(credentials.privateKey(), credentials.certificate())
                .flatMap(signature -> Mono.fromCallable(() -> {
                    try (var outputStream = new ByteArrayOutputStream()) {
                        signature.open(outputStream);
                        signature.update(data);
                        signature.close();
                        return outputStream.toByteArray();
                    }
                }));
    }

    public Mono<String> encode(byte[] data) {
        return Mono.fromCallable(() -> new Encoder().encode(data))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
