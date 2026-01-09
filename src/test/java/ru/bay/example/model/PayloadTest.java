package ru.bay.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.bay.example.config.property.PayloadProperties;
import ru.bay.example.data.PlainProfileAttributesChange;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.bay.example.data.Constants.TEST_ATTRIBUTES;

class PayloadTest {
    private Payload payload;

    @BeforeEach
    void setUp() {
        payload = new Payload(new PayloadProperties(TEST_ATTRIBUTES));
    }

    @Test
    void test() {
        Mono<byte[]> mono = payload.defaultPayload();

        StepVerifier.create(mono).assertNext(bytes -> {
            assertNotNull(bytes);
            assertArrayEquals(PlainProfileAttributesChange.CN.getBytes(StandardCharsets.UTF_8), bytes);
        }).verifyComplete();
    }
}