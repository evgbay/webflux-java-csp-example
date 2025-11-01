package ru.bay.example.exception;

@Deprecated(forRemoval = true)
public class SigningOperationException extends RuntimeException {
    public SigningOperationException(String message) {
        super(message);
    }
}
