package ru.bay.example.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "app.payload")
public record PayloadProperties(Map<String, String> attributes) {
}
