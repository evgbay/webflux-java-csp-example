package ru.bay.example.service.provider;

import org.junit.jupiter.api.Test;
import ru.bay.example.data.Give;

import java.security.KeyPair;

import static org.assertj.core.api.Assertions.assertThat;

class KeyPairTest {
    @Test
    void shouldReturnValidDummyKeyPair() {
        var keyPair = Give.keyPair().dummy();

        assertThat(keyPair).satisfies(pair -> {
            assertThat(pair).isNotNull();
            assertThat(pair).isInstanceOf(KeyPair.class);
            assertThat(pair.getPublic()).isNotNull();
            assertThat(pair.getPrivate()).isNotNull();
        });
    }

    @Test
    void shouldReturnValidGostKeyPair() {
        var keyPair = Give.keyPair().gost();

        assertThat(keyPair).satisfies(pair -> {
            assertThat(pair).isNotNull();
            assertThat(pair).isInstanceOf(KeyPair.class);
            assertThat(pair.getPublic()).isNotNull();
            assertThat(pair.getPrivate()).isNotNull();
        });
    }
}
