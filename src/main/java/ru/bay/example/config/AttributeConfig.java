package ru.bay.example.config;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttributeConfig {
    private static final String COMMITMENT_TYPE_INDICATION = "1.2.840.113549.1.9.16.2.16";
    private static final String ID_CTI_ETS_PROOF_OF_APPROVAL = "1.2.840.113549.1.9.16.6.5";

    @Bean
    public Attribute approval() {
        var vector = new ASN1EncodableVector();
        vector.add(new ASN1ObjectIdentifier(ID_CTI_ETS_PROOF_OF_APPROVAL));
        return new Attribute(
                new ASN1ObjectIdentifier(COMMITMENT_TYPE_INDICATION),
                new DERSet(new DERSequence(vector))
        );
    }
}
