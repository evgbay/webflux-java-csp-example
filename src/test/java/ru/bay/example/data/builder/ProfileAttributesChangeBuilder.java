package ru.bay.example.data.builder;

import ru.bay.example.model.ProfileAttributesChange;

import java.util.HashMap;
import java.util.Map;

public class ProfileAttributesChangeBuilder {
    private final Map<String, String> attributes = new HashMap<>();

    public ProfileAttributesChangeBuilder withAttribute(final String key, final String value) {
        attributes.put(key, value);
        return this;
    }

    public ProfileAttributesChange build() {
        return ProfileAttributesChange.from(attributes);
    }
}
