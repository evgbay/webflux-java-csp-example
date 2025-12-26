package ru.bay.example.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record DistinguishedName(
        String fullName,
        String company,
        String inn,
        String country,
        String city,
        String area,
        String address,
        String lastName,
        String firstName,
        String snils,
        String position,
        String email
) {
}
