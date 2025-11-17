package ru.bay.example.config.property;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.crypto")
@Validated
public record CryptoProperties(
        @NotBlank String type,
        @NotBlank String path,
        @NotBlank String alias,
        @NotBlank String password
) {
}
