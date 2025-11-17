package ru.bay.example.data.builder;

import ru.bay.example.data.Give;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static ru.bay.example.data.Constants.ERR_KEY_PAIR_MESSAGE;


public class RSAKeyPairBuilder {
    private static final String ALGORITHM = "RSA";
    private static final KeyPair KEY_PAIR = genKeyPair();

    public static KeyPair get() {
        return KEY_PAIR;
    }

    private static KeyPair genKeyPair() {
        try {
            var generator = KeyPairGenerator.getInstance(ALGORITHM, Give.provider());
            generator.initialize(2048, Give.secureRandom());
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(String.format(ERR_KEY_PAIR_MESSAGE, ALGORITHM), e);
        }
    }
}
