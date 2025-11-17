package ru.bay.example.service.provider;

import org.bouncycastle.asn1.cms.Attribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static ru.bay.example.data.Constants.EXPECTED_ENCODED_PHRASE;
import static ru.bay.example.data.Constants.PHRASE;

@ExtendWith(MockitoExtension.class)
class CAdESSignatureProviderTest {
    @Mock
    private Attribute attribute;
    private CAdESSignatureProvider provider;

    @BeforeEach
    void setUp() {
        provider = new CAdESSignatureProvider(attribute);
    }

    @Test
    void shouldReturnEncodedData() {
        byte[] data = PHRASE.getBytes(StandardCharsets.UTF_8);

        Mono<String> mono = provider.encode(data);

        StepVerifier.create(mono).expectNext(EXPECTED_ENCODED_PHRASE).verifyComplete();
    }
}