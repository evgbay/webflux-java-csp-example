package ru.bay.example.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.bay.example.config.property.PayloadProperties;
import ru.bay.example.util.XmlEncoder;

@Component
@RequiredArgsConstructor
public final class Payload {
    private final PayloadProperties payloadProperties;

    public Mono<byte[]> defaultPayload() {
        var profileAttributesChange = ProfileAttributesChange.from(payloadProperties.attributes());
        return Mono.fromCallable(() -> XmlEncoder.pacToBytes(profileAttributesChange));
    }
}
