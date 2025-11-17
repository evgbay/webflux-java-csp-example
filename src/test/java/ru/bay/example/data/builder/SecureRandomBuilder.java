package ru.bay.example.data.builder;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

public class SecureRandomBuilder {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom(generateSeed());

    public static SecureRandom get() {
        return SECURE_RANDOM;
    }

    private static byte[] generateSeed() {
        var uuid = UUID.randomUUID().toString();
        return uuid.getBytes(StandardCharsets.UTF_8);
    }
}
