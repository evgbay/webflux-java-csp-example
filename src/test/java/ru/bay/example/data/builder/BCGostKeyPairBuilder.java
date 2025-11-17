package ru.bay.example.data.builder;

import ru.bay.example.data.Give;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;

import static ru.bay.example.data.Constants.ERR_KEY_PAIR_MESSAGE;

public class BCGostKeyPairBuilder {
    private static final String ALGORITHM = "ECGOST3410-2012";
    private static final KeyPair KEY_PAIR = genKeyPair();

    public static KeyPair get() {
        return KEY_PAIR;
    }

    private static KeyPair genKeyPair() {
        try {
            var generator = KeyPairGenerator.getInstance(ALGORITHM, Give.provider());
            generator.initialize(new ECGenParameterSpec("Tc26-Gost-3410-12-512-paramSetA"));
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(String.format(ERR_KEY_PAIR_MESSAGE, ALGORITHM), e);
        }
    }
}
