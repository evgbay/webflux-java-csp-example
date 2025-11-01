package ru.bay.example.exception;

@Deprecated(forRemoval = true)
public class CAdESSignatureException extends RuntimeException {
    public CAdESSignatureException(String message) {
        super(message);
    }
}
