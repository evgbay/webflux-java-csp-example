package ru.bay.example.util;

import lombok.experimental.UtilityClass;
import ru.bay.example.model.ProfileAttributesChange;
import tools.jackson.dataformat.xml.XmlMapper;

@UtilityClass
public class XmlEncoder {
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    public static byte[] pacToBytes(ProfileAttributesChange pac) {
        return XML_MAPPER.writeValueAsBytes(pac);
    }
}
