package ru.bay.example.service.provider;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.CryptoPro.AdES.Options;
import ru.CryptoPro.CAdES.CAdESSignature;
import ru.CryptoPro.CAdES.CAdESType;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

@Component
public class CAdESSignatureProvider {
    private static final String PROVIDER_NAME = "JCSP";

    @SuppressWarnings("java:S3252")
    public Mono<CAdESSignature> newInstance(PrivateKey privateKey, X509Certificate certificate) {
        return Mono.fromCallable(() -> {
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
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
