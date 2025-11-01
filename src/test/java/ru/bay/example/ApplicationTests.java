package ru.bay.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {
    private final ApplicationContext context;

    public ApplicationTests(ApplicationContext context) {
        this.context = context;
    }


    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }
}
