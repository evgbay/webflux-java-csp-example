package ru.bay.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.security.KeyStore;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {
    private final ApplicationContext context;
    @MockitoBean
    private KeyStore pfxKeyStore;

    public ApplicationTests(ApplicationContext context) {
        this.context = context;
    }

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }
}
