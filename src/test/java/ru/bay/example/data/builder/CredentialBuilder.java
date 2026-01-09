package ru.bay.example.data.builder;

import ru.bay.example.data.Give;
import ru.bay.example.model.Credentials;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

public class CredentialBuilder {
    private KeyPair pair;
    private X509Certificate certificate;

    public CredentialBuilder gost() {
        this.pair = Give.keyPair().gost();
        this.certificate = Give.certificate().dummy();
        return this;
    }

    public Credentials build() {
        return new Credentials(pair.getPrivate(), certificate);
    }
}
