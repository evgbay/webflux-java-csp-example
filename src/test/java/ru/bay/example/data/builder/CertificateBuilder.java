package ru.bay.example.data.builder;

import java.security.cert.X509Certificate;

public class CertificateBuilder {
    public X509Certificate dummy() {
        return BCCertificateBuilder.get();
    }
}
