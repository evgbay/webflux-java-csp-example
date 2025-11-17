package ru.bay.example.model;

import reactor.util.function.Tuple2;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public record Credentials(
        PrivateKey privateKey,
        X509Certificate certificate
) {
    public static Credentials from(Tuple2<PrivateKey, X509Certificate> tuple) {
        return new Credentials(tuple.getT1(), tuple.getT2());
    }
}
