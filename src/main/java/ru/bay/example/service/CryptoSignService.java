package ru.bay.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.bay.example.model.Credentials;
import ru.bay.example.service.provider.AttributePolicy;
import ru.bay.example.service.provider.CAdESSignatureProvider;
import ru.bay.example.service.provider.CredentialProvider;
import ru.bay.example.util.CryptoEncoder;

import static ru.bay.example.service.provider.AttributePolicy.APPROVAL;
import static ru.bay.example.service.provider.AttributePolicy.NONE;


@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoSignService {
    private static final String ERR_UNEXPECTED = "Unexpected error during sign operation";

    private final CredentialProvider credentialProvider;
    private final CAdESSignatureProvider cadesSignatureProvider;

    public Mono<String> privilegeSignature(byte[] data) {
        return credentialProvider.assembleCredentials()
                .flatMap(credentials -> signOnce(data, credentials, APPROVAL))
                .flatMap(CryptoEncoder::encode)
                .onErrorResume(this::handleException);
    }

    public Mono<String> doubleSignature(byte[] data) {
        return credentialProvider.assembleCredentials()
                .flatMap(credentials -> doubleSign(data, credentials, NONE))
                .flatMap(CryptoEncoder::encode)
                .onErrorResume(this::handleException);
    }

    public Mono<byte[]> signOnce(byte[] data, Credentials credentials, AttributePolicy policy) {
        return cadesSignatureProvider.newInstance(credentials, policy)
                .flatMap(signature -> cadesSignatureProvider.applySignature(data, signature));
    }

    public Mono<byte[]> doubleSign(byte[] data, Credentials credentials, AttributePolicy policy) {
        return signOnce(data, credentials, policy)
                .flatMap(tempBytes -> signOnce(tempBytes, credentials, policy));
    }

    private Mono<String> handleException(Throwable ex) {
        log.error(ERR_UNEXPECTED, ex);
        return Mono.just(ex.getMessage());
    }
}
