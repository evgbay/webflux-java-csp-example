package ru.bay.example.util;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;
import ru.CryptoPro.JCP.tools.Encoder;

@UtilityClass
public class CryptoEncoder {
    public Mono<String> encode(byte[] data) {
        return Mono.fromCallable(() -> new Encoder().encode(data));
    }
}
