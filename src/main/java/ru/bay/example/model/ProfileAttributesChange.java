package ru.bay.example.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonRootName(value = "ProfileAttributesChange")
public record ProfileAttributesChange(
        @JacksonXmlProperty(localName = "To")
        To to
) {

    public record To(
            @JacksonXmlElementWrapper(useWrapping = false)
            @JacksonXmlProperty(localName = "Attribute")
            List<Attribute> attributes
    ) {
    }

    public record Attribute(
            @JacksonXmlProperty(isAttribute = true, localName = "Oid")
            String oid,
            @JacksonXmlProperty(isAttribute = true, localName = "Value")
            String value
    ) {
    }

    public static ProfileAttributesChange from(Map<String, String> rawAttributes) {
        final var attributes = new ArrayList<Attribute>();
        rawAttributes.forEach((key, value) -> attributes.add(new Attribute(key, value)));
        return new ProfileAttributesChange(new To(attributes));
    }
}