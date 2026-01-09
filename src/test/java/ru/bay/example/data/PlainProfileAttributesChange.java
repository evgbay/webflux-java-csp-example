package ru.bay.example.data;

import static ru.bay.example.data.Constants.*;

public interface PlainProfileAttributesChange {
    String CN =
            String.format("<ProfileAttributesChange><To><Attribute Oid=\"%s\" Value=\"%s\"/></To></ProfileAttributesChange>",
                    CN_OID, ALIAS);
    String SEVERAL_VALUES =
            String.format("<ProfileAttributesChange><To><Attribute Oid=\"%s\" Value=\"%s\"/>" +
                            "<Attribute Oid=\"%s\" Value=\"%s\"/><Attribute Oid=\"%s\" Value=\"%s\"/>" +
                            "<Attribute Oid=\"%s\" Value=\"%s\"/></To></ProfileAttributesChange>",
                    COUNTRY_OID, COUNTRY,
                    CN_OID, ALIAS,
                    EMAIL_OID, EMAIL,
                    CITY_OID, CITY
            );
}
