package ru.bay.example.data.builder;

import ru.bay.example.data.Give;
import ru.bay.example.exception.KeyStoreOperationException;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import static ru.bay.example.data.Constants.ALIAS;
import static ru.bay.example.data.Constants.PASSWORD;

public class KeyStoreBuilder {
    private final Map<String, KeyStore> cache = new HashMap<>();

    public KeyStore dummy() {
        try {
            if (cache.containsKey(ALIAS)) {
                return cache.get(ALIAS);
            }
            var dummyStore = KeyStore.getInstance("PKCS12");
            dummyStore.load(null, null);
            var pair = Give.keyPair().dummy();
            var cert = Give.certificate().dummy();
            dummyStore.setKeyEntry(ALIAS, pair.getPrivate(), PASSWORD.toCharArray(), new Certificate[]{cert});
            cache.put(ALIAS, dummyStore);
            return dummyStore;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new KeyStoreOperationException("An exception occurred while creating the dummy store");
        }
    }
}
