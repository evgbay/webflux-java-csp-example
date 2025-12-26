package ru.bay.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.bay.example.model.Payload;
import ru.bay.example.service.CryptoSignService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.TEXT_PLAIN_VALUE)
@RequiredArgsConstructor
public class CryptoController {
    private final Payload payload;
    private final CryptoSignService cryptoSignService;

    @GetMapping("/v1/sign")
    public Mono<String> privilegeSignature() {
        return payload.defaultPayload()
                .flatMap(cryptoSignService::privilegeSignature)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/v2/sign")
    public Mono<String> doubleSignature() {
        return payload.defaultPayload()
                .flatMap(cryptoSignService::doubleSignature)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
