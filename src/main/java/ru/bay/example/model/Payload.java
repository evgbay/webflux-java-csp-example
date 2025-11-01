package ru.bay.example.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bay.example.config.property.PayloadProperties;
import ru.bay.example.util.XmlEncoder;

@Component
@RequiredArgsConstructor
public final class Payload {
    private final PayloadProperties payloadProperties;

    public byte[] getBytes() {
        return XmlEncoder.pacToBytes(ProfileAttributesChange.from(payloadProperties.attributes()));
    }
}
