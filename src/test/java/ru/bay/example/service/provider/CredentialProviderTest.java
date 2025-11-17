package ru.bay.example.service.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.bay.example.config.property.CryptoProperties;
import ru.bay.example.data.Give;
import ru.bay.example.model.Credentials;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ru.bay.example.data.Constants.ALIAS;
import static ru.bay.example.data.Constants.PASSWORD;

@ExtendWith(MockitoExtension.class)
class CredentialProviderTest {
    @Mock
    private CryptoProperties properties;
    private CredentialProvider provider;

    @BeforeEach
    void setUp() {
        KeyStore pfxKeyStore = Give.keyStore().dummy();
        this.provider = new CredentialProvider(pfxKeyStore, properties);
    }

    @Test
    void shouldReturnPrivateKeyFromKeyStore() {
        when(properties.alias())
                .thenReturn(ALIAS);
        when(properties.password())
                .thenReturn(PASSWORD);

        Mono<PrivateKey> mono = provider.getKey();

        StepVerifier.create(mono)
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result).isInstanceOf(PrivateKey.class);
                }).verifyComplete();
    }

    @Test
    void shouldReturnX509CertificateFromKeyStore() {
        when(properties.alias())
                .thenReturn(ALIAS);

        Mono<X509Certificate> mono = provider.getCertificate();

        StepVerifier.create(mono)
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result).isInstanceOf(X509Certificate.class);
                }).verifyComplete();
    }

    @Test
    void shouldReturnCredentials() {
        when(properties.alias())
                .thenReturn(ALIAS);
        when(properties.password())
                .thenReturn(PASSWORD);

        Mono<Credentials> mono = provider.assembleCredentials();

        StepVerifier.create(mono)
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result).isInstanceOf(Credentials.class);
                    assertThat(result.privateKey()).isNotNull();
                    assertThat(result.certificate())
                            .isNotNull()
                            .isInstanceOf(X509Certificate.class);
                }).verifyComplete();
    }
}