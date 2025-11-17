package ru.bay.example.data.builder;

import java.security.KeyPair;

public class KeyPairBuilder {
    public KeyPair dummy() {
        return RSAKeyPairBuilder.get();
    }

    public KeyPair gost() {
        return BCGostKeyPairBuilder.get();
    }
}
