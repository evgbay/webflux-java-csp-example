package ru.bay.example.exception;

public class KeyStoreOperationException extends RuntimeException {
    public KeyStoreOperationException(String message) {
        super(message);
    }
}
