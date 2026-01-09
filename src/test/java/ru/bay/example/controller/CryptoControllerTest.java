package ru.bay.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.bay.example.model.Payload;
import ru.bay.example.service.CryptoSignService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.bay.example.data.Constants.EXPECTED_ENCODED_PHRASE;
import static ru.bay.example.data.Constants.PHRASE;

@WebFluxTest(value = CryptoController.class)
class CryptoControllerTest {
    @MockitoBean
    private CryptoSignService cryptoSignService;
    @MockitoBean
    private Payload payload;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldReturnTestedStringAfterPrivilegeSign() {
        when(payload.defaultPayload())
                .thenReturn(Mono.just(PHRASE.getBytes(StandardCharsets.UTF_8)));
        when(cryptoSignService.privilegeSignature(any()))
                .thenReturn(Mono.just(EXPECTED_ENCODED_PHRASE));

        webTestClient.get()
                .uri("/api/v1/sign")
                .exchange()
                .expectBody(String.class)
                .isEqualTo(EXPECTED_ENCODED_PHRASE);
    }

    @Test
    void shouldReturnTestedStringAfterDoubleSign() {
        when(payload.defaultPayload())
                .thenReturn(Mono.just(PHRASE.getBytes(StandardCharsets.UTF_8)));
        when(cryptoSignService.doubleSignature(any()))
                .thenReturn(Mono.just(EXPECTED_ENCODED_PHRASE));

        webTestClient.get()
                .uri("/api/v2/sign")
                .exchange()
                .expectBody(String.class)
                .isEqualTo(EXPECTED_ENCODED_PHRASE);
    }
}