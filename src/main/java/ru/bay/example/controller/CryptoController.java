package ru.bay.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bay.example.model.Payload;
import ru.bay.example.service.CryptoService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CryptoController {
    private final Payload payload;
    private final CryptoService cryptoService;

    @GetMapping("/sign")
    public Mono<String> testSignature() {
        return Mono.fromCallable(payload::getBytes)
                .flatMap(cryptoService::sign);
    }
}
