package ru.bay.example.model;

public record KeyStoreStatusResult(
        KeyStoreStatus status,
        String message
) {
    public static KeyStoreStatusResult ok() {
        return new KeyStoreStatusResult(KeyStoreStatus.OK, "");
    }

    public static KeyStoreStatusResult error(String message) {
        return new KeyStoreStatusResult(KeyStoreStatus.ERROR, message);
    }
}
