package ru.bay.example.util;

import org.junit.jupiter.api.Test;
import ru.bay.example.data.Give;
import ru.bay.example.data.PlainProfileAttributesChange;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.bay.example.data.Constants.*;

class XmlEncoderTest {
    @Test
    void shouldReturnValidProfileAttributesChangeWithOneAttribute() {
        var pac = Give.pac()
                .withAttribute(CN_OID, ALIAS)
                .build();

        byte[] bytes = XmlEncoder.toBytes(pac);

        assertThat(new String(bytes)).isEqualTo(PlainProfileAttributesChange.CN);
    }

    @Test
    void shouldReturnValidProfileAttributesChangeWithSeveralAttribute() {
        var pac = Give.pac()
                .withAttribute(CN_OID, ALIAS)
                .withAttribute(COUNTRY_OID, COUNTRY)
                .withAttribute(CITY_OID, CITY)
                .withAttribute(EMAIL_OID, EMAIL)
                .build();

        byte[] bytes = XmlEncoder.toBytes(pac);

        assertThat(new String(bytes)).isEqualTo(PlainProfileAttributesChange.SEVERAL_VALUES);
    }
}