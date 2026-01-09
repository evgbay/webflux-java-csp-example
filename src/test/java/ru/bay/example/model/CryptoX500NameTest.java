package ru.bay.example.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static ru.bay.example.data.Constants.ALIAS;
import static ru.bay.example.data.Constants.CN_OID;

class CryptoX500NameTest {
    @Test
    void shouldReturnDNParseX500Name() {
        var dn = "CN=Вагнер Тамара Олеговна, C=RU, L=Москва, S=Республика Адыгея, STREET=Ленина 73, E=User641@pochtochka.com";
        var x500Name = CryptoX500Name.builder()
                .addRDN(CryptoStyle.CN, "Вагнер Тамара Олеговна")
                .addRDN(CryptoStyle.C, "RU")
                .addRDN(CryptoStyle.L, "Москва")
                .addRDN(CryptoStyle.S, "Республика Адыгея")
                .addRDN(CryptoStyle.STREET, "Ленина 73")
                .addRDN(CryptoStyle.E, "User641@pochtochka.com")
                .build();
        assertThat(dn).isEqualTo(x500Name.toDN());
    }

    @Test
    void shouldReturnX500NameParseDN() {
        var dn = "CN=Вагнер Тамара Олеговна, C=RU, L=Москва, S=Республика Адыгея, STREET=Ленина 73, E=User641@pochtochka.com";
        var expectedCryptoX500Name = CryptoX500Name.builder()
                .addRDN(CryptoStyle.CN, "Вагнер Тамара Олеговна")
                .addRDN(CryptoStyle.C, "RU")
                .addRDN(CryptoStyle.L, "Москва")
                .addRDN(CryptoStyle.S, "Республика Адыгея")
                .addRDN(CryptoStyle.STREET, "Ленина 73")
                .addRDN(CryptoStyle.E, "User641@pochtochka.com")
                .build();
        assertThat(expectedCryptoX500Name).isEqualTo(CryptoX500Name.parse(dn));
    }

    @Test
    void shouldReturnX500NameParseDistinguishedName() {
        var expectedCryptoX500Name = CryptoX500Name.builder()
                .addRDN(CryptoStyle.CN, "Вагнер Тамара Олеговна")
                .addRDN(CryptoStyle.C, "RU")
                .addRDN(CryptoStyle.L, "Москва")
                .addRDN(CryptoStyle.S, "Республика Адыгея")
                .addRDN(CryptoStyle.STREET, "Ленина 73")
                .addRDN(CryptoStyle.E, "User641@pochtochka.com")
                .build();
        var dn = DistinguishedName.builder()
                .fullName("Вагнер Тамара Олеговна")
                .country("RU")
                .city("Москва")
                .area("Республика Адыгея")
                .address("Ленина 73")
                .email("User641@pochtochka.com")
                .build();
        assertThat(expectedCryptoX500Name).isEqualTo(CryptoX500Name.from(dn));
    }

    @Test
    void shouldReturnAttributesParseX500Name() {
        var cryptoX500Name = CryptoX500Name.builder()
                .addRDN(CryptoStyle.CN, "Вагнер Тамара Олеговна")
                .addRDN(CryptoStyle.C, "RU")
                .addRDN(CryptoStyle.L, "Москва")
                .addRDN(CryptoStyle.S, "Республика Адыгея")
                .addRDN(CryptoStyle.STREET, "Ленина 73")
                .addRDN(CryptoStyle.E, "User641@pochtochka.com")
                .build();
        var attributes = cryptoX500Name.toAttributes();
        assertThat(attributes).contains(
                entry(CryptoStyle.CN.oid(), "Вагнер Тамара Олеговна"),
                entry(CryptoStyle.C.oid(), "RU"),
                entry(CryptoStyle.L.oid(), "Москва")
        );
    }

    @Test
    void shouldReturnProfileAttributesChange() {
        var expected = ProfileAttributesChange.from(Map.of(CN_OID, ALIAS));

        var x500Name = CryptoX500Name.builder()
                .addRDN(CryptoStyle.CN, ALIAS)
                .build();
        var actual = ProfileAttributesChange.from(x500Name.toAttributes());


        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnIdenticalStringRepresentationOfX500Name() {
        var firstName = CryptoX500Name.builder()
                .addRDN(CryptoStyle.CN, ALIAS)
                .addRDN(CryptoStyle.C, "RU")
                .addRDN(CryptoStyle.L, "Москва")
                .addRDN(CryptoStyle.S, "Республика Адыгея")
                .build();
        var secondName = CryptoX500Name.builder()
                .addRDN(CryptoStyle.CN, ALIAS)
                .addRDN(CryptoStyle.C, "RU")
                .addRDN(CryptoStyle.L, "Москва")
                .addRDN(CryptoStyle.S, "Республика Адыгея")
                .build();
        assertThat(firstName).isEqualTo(secondName);
        assertThat(firstName.toString()).hasToString(secondName.toString());
    }
}
