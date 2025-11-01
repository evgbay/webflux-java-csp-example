package ru.bay.example.model;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public record Credentials(
        PrivateKey privateKey,
        X509Certificate certificate
) {
    public static Credentials from(PrivateKey privateKey, X509Certificate certificate) {
        return new Credentials(privateKey, certificate);
    }
}
