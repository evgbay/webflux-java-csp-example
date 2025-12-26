package ru.bay.example.model;

import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record CryptoX500Name(String[] rdns) {
    private static final String SEPARATOR = ", ";
    private static final String DELIMITER = "=";

    public static CryptoX500Name parse(String dn) {
        return new CryptoX500Name(dn.split(SEPARATOR));
    }

    public static CryptoX500Name from(DistinguishedName dn) {
        return CryptoX500Name.builder()
                .addRDN(CryptoStyle.CN, dn.fullName())
                .addRDN(CryptoStyle.C, dn.country())
                .addRDN(CryptoStyle.L, dn.city())
                .addRDN(CryptoStyle.S, dn.area())
                .addRDN(CryptoStyle.STREET, dn.address())
                .addRDN(CryptoStyle.INNLE, dn.inn())
                .addRDN(CryptoStyle.E, dn.email())
                .build();
    }

    public static CryptoX500NameBuilder builder() {
        return new CryptoX500NameBuilder();
    }

    public String toDN() {
        return String.join(SEPARATOR, rdns);
    }

    public Map<String, String> toAttributes() {
        return Arrays.stream(rdns)
                .map(rnd -> rnd.split(DELIMITER, 2))
                .collect(Collectors.toMap(arr -> CryptoStyle.valueOf(arr[0]).oid(), arr -> arr[1]));
    }

    public boolean equals(Object o) {
        return o instanceof CryptoX500Name(String[] arr)
                && Arrays.equals(rdns, arr);
    }

    public int hashCode() {
        return Arrays.hashCode(rdns);
    }

    @Override
    @NonNull
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(rdns);
    }

    public static class CryptoX500NameBuilder {
        private final ArrayList<String> buffer = new ArrayList<>();

        public CryptoX500NameBuilder addRDN(CryptoStyle style, String value) {
            if (Objects.nonNull(value) && !value.trim().isEmpty()) {
                buffer.add(style.name() + DELIMITER + value);
            }
            return this;
        }

        public CryptoX500Name build() {
            return new CryptoX500Name(buffer.toArray(new String[0]));
        }
    }
}
