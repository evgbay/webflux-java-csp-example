package ru.bay.example.service.provider;

import org.junit.jupiter.api.Test;
import ru.bay.example.data.Give;

import static org.assertj.core.api.Assertions.assertThat;

class KeyStoreTest {
    @Test
    void shouldReturnValidDummyStore() {
        var dummyStore = Give.keyStore().dummy();

        assertThat(dummyStore).isNotNull();
    }
}
