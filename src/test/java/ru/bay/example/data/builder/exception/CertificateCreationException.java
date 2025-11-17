package ru.bay.example.data.builder.exception;

public class CertificateCreationException extends RuntimeException {
    private static final String STEP_MESSAGE = "Certificate creation failed at %s step with exception - %s";

    public CertificateCreationException(CertificateCreationStep step, Throwable cause) {
        super(String.format(STEP_MESSAGE, step.name(), cause.getMessage()), cause);
    }
}
