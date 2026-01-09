package ru.bay.example.util;

import lombok.experimental.UtilityClass;
import tools.jackson.dataformat.xml.XmlMapper;

@UtilityClass
public class XmlEncoder {
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    public static <T> byte[] toBytes(T instance) {
        return XML_MAPPER.writeValueAsBytes(instance);
    }
}
