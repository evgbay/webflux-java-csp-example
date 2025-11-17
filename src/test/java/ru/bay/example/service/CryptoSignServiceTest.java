package ru.bay.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.CryptoPro.CAdES.CAdESSignature;
import ru.CryptoPro.CAdES.exception.CAdESException;
import ru.bay.example.data.Give;
import ru.bay.example.model.Credentials;
import ru.bay.example.service.provider.AttributePolicy;
import ru.bay.example.service.provider.CAdESSignatureProvider;
import ru.bay.example.service.provider.CredentialProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.bay.example.data.Constants.PHRASE;


@ExtendWith(MockitoExtension.class)
class CryptoSignServiceTest {
    @Mock
    private CredentialProvider credentialProvider;
    @Mock
    private CAdESSignatureProvider cadesSignatureProvider;
    @InjectMocks
    private CryptoSignService service;

    @Test
    void shouldReturnExceptionMessageWhenKeyStoreValidationEndWithException() {
        var exceptionMessage = "Something went wrong";

        when(credentialProvider.assembleCredentials())
                .thenReturn(Mono.error(new RuntimeException(exceptionMessage)));

        Mono<String> mono = service.signV2(PHRASE.getBytes());

        StepVerifier.create(mono)
                .expectNext(exceptionMessage)
                .verifyComplete();
    }

    @Test
    void shouldReturnSignedBytesAfterSignOne() throws CAdESException {
        var pair = Give.keyPair().gost();
        var certificate = Give.certificate().dummy();
        var credentials = new Credentials(pair.getPrivate(), certificate);

        when(cadesSignatureProvider.newInstance(any(Credentials.class), any(AttributePolicy.class)))
                .thenReturn(Mono.just(new CAdESSignature()));
        when(cadesSignatureProvider.applySignature(any(), any(CAdESSignature.class)))
                .thenReturn(Mono.just(PHRASE.getBytes()));

        Mono<byte[]> mono = service.signOnce(PHRASE.getBytes(), credentials, AttributePolicy.NONE);

        StepVerifier.create(mono)
                .assertNext(result -> assertArrayEquals(PHRASE.getBytes(), result))
                .verifyComplete();

        InOrder inOrder = inOrder(cadesSignatureProvider);
        inOrder.verify(cadesSignatureProvider).newInstance(any(Credentials.class), any(AttributePolicy.class));
        inOrder.verify(cadesSignatureProvider).applySignature(any(), any(CAdESSignature.class));
    }

    @Test
    void shouldReturnSignedBytesAfterSignOneWithAttribute() throws CAdESException {
        var pair = Give.keyPair().gost();
        var certificate = Give.certificate().dummy();
        var credentials = new Credentials(pair.getPrivate(), certificate);

        when(cadesSignatureProvider.newInstance(any(Credentials.class), any(AttributePolicy.class)))
                .thenReturn(Mono.just(new CAdESSignature()));
        when(cadesSignatureProvider.applySignature(any(), any(CAdESSignature.class)))
                .thenReturn(Mono.just(PHRASE.getBytes()));


        Mono<byte[]> mono = service.signOnce(PHRASE.getBytes(), credentials, AttributePolicy.APPROVAL);

        StepVerifier.create(mono)
                .assertNext(result -> assertArrayEquals(PHRASE.getBytes(), result))
                .verifyComplete();

        verify(cadesSignatureProvider).newInstance(any(Credentials.class), any(AttributePolicy.class));
        verify(cadesSignatureProvider).applySignature(any(), any(CAdESSignature.class));
    }

    @Test
    void shouldReturnSignedBytesAfterDoubleSign() throws CAdESException {
        var pair = Give.keyPair().gost();
        var certificate = Give.certificate().dummy();
        var credentials = new Credentials(pair.getPrivate(), certificate);

        when(cadesSignatureProvider.newInstance(any(Credentials.class), any(AttributePolicy.class)))
                .thenReturn(Mono.just(new CAdESSignature()));
        when(cadesSignatureProvider.applySignature(any(), any(CAdESSignature.class)))
                .thenReturn(Mono.just(PHRASE.getBytes()));

        Mono<byte[]> mono = service.doubleSign(PHRASE.getBytes(), credentials, AttributePolicy.NONE);

        StepVerifier.create(mono)
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertArrayEquals(PHRASE.getBytes(), result);
                })
                .verifyComplete();

        verify(cadesSignatureProvider, times(2)).newInstance(any(Credentials.class), any(AttributePolicy.class));
        verify(cadesSignatureProvider, times(2)).applySignature(any(), any(CAdESSignature.class));
    }
}