package ru.bay.example.service.provider;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.CryptoPro.AdES.Options;
import ru.CryptoPro.CAdES.CAdESSignature;
import ru.CryptoPro.CAdES.CAdESType;
import ru.CryptoPro.JCSP.JCSP;
import ru.bay.example.model.Credentials;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CAdESSignatureProvider {
    private final Attribute approval;

    public Mono<CAdESSignature> newInstance(Credentials credentials, AttributePolicy policy) {
        return from(credentials, policy);
    }

    public Mono<byte[]> applySignature(byte[] data, CAdESSignature signature) {
        return Mono.fromCallable(() -> {
            try (var outputStream = new ByteArrayOutputStream()) {
                signature.open(outputStream);
                signature.update(data);
                signature.close();
                return outputStream.toByteArray();
            }
        });
    }

    @SuppressWarnings("java:S3252")
    private Mono<CAdESSignature> from(Credentials credentials, AttributePolicy policy) {
        AttributeTable table = switch (policy) {
            case NONE -> null;
            case APPROVAL -> new AttributeTable(approval);
        };

        return Mono.fromCallable(() -> {
            var signature = new CAdESSignature();
            signature.setOptions(new Options().disableCertificateValidation());
            // @formatter:off
            signature.addSigner(
                    JCSP.PROVIDER_NAME,
                    null,
                    null,
                    credentials.privateKey(),
                    List.of(credentials.certificate()),
                    CAdESType.CAdES_BES,
                    null,
                    false,
                    table,
                    null,
                    null,
                    false);
            // @formatter:on
            return signature;
        });
    }
}
