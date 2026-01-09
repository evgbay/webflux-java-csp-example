package ru.bay.example.data;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import ru.bay.example.data.builder.*;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

public class Give {
    private static final Provider PROVIDER = new BouncyCastleProvider();

    static {
        Security.insertProviderAt(PROVIDER, 1);
    }

    public static Provider provider() {
        return PROVIDER;
    }

    public static SecureRandom secureRandom() {
        return SecureRandomBuilder.get();
    }

    public static KeyStoreBuilder keyStore() {
        return new KeyStoreBuilder();
    }

    public static KeyPairBuilder keyPair() {
        return new KeyPairBuilder();
    }

    public static CertificateBuilder certificate() {
        return new CertificateBuilder();
    }

    public static CredentialBuilder credentials() {
        return new CredentialBuilder();
    }

    public static ProfileAttributesChangeBuilder pac() {
        return new ProfileAttributesChangeBuilder();
    }
}
