package ru.bay.example.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.crypto")
public record CryptoProperties(
        String type,
        String path,
        String alias,
        String password
) {
}
