package ru.bay.example.util;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.bay.example.data.Constants.EXPECTED_ENCODED_PHRASE;
import static ru.bay.example.data.Constants.PHRASE;

class CryptoEncoderTest {
    @Test
    void shouldReturnEncodedString() {
        Mono<String> mono = CryptoEncoder.encode(PHRASE.getBytes(StandardCharsets.UTF_8));

        StepVerifier.create(mono)
                .assertNext(str -> {
                    assertNotNull(str);
                    assertEquals(EXPECTED_ENCODED_PHRASE, str);
                })
                .verifyComplete();
    }
}