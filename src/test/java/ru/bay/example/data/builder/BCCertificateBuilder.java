package ru.bay.example.data.builder;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.bc.BcX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import ru.bay.example.data.Give;
import ru.bay.example.data.builder.exception.CertificateCreationException;
import ru.bay.example.data.builder.exception.CertificateCreationStep;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.ZonedDateTime;
import java.util.Date;

import static ru.bay.example.data.Constants.*;


public class BCCertificateBuilder {
    private static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.now();
    private static final int YEARS_OF_CERTIFICATE_VALIDITY = 5;
    private static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";
    private static final X509Certificate CERTIFICATE = getSelfSignedCertificate();

    public static X509Certificate get() {
        return CERTIFICATE;
    }

    private static X509Certificate getSelfSignedCertificate() {
        var pair = Give.keyPair().dummy();
        byte[] bytes = encodePublicKey(pair);
        // issuer (publisher, who) == subject (to whom)
        var issuer = getSubject();
        var secureRandom = Give.secureRandom();
        var serialNumber = new BigInteger(Long.SIZE, secureRandom);
        var info = getSubjectPublicKeyInfo(bytes);
        var builder = getCertificateBuilder(issuer, serialNumber, issuer, info);
        var signer = getContentSigner(pair);
        var holder = getCertificateHolder(builder, info, signer);
        return newInstance(holder);
    }

    private static byte[] encodePublicKey(KeyPair pair) {
        return pair.getPublic().getEncoded();
    }

    private static X500Name getSubject() {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.CN, ALIAS)
                .addRDN(BCStyle.L, CITY)
                .addRDN(BCStyle.ST, STATE)
                .addRDN(BCStyle.C, COUNTRY);
        return builder.build();
    }

    private static SubjectPublicKeyInfo getSubjectPublicKeyInfo(byte[] bytes) {
        try (var byteStream = new ByteArrayInputStream(bytes)) {
            try (var asn1Stream = new ASN1InputStream(byteStream)) {
                var sequence = (ASN1Sequence) asn1Stream.readObject();
                return SubjectPublicKeyInfo.getInstance(sequence);
            }
        } catch (IOException e) {
            throw new CertificateCreationException(CertificateCreationStep.GET_SUBJECT_PK_INFO, e);
        }
    }

    private static X509v3CertificateBuilder getCertificateBuilder(
            X500Name issuer,
            BigInteger serialNumber,
            X500Name subject,
            SubjectPublicKeyInfo info
    ) {
        return new X509v3CertificateBuilder(
                issuer,
                serialNumber,
                from(ZONED_DATE_TIME),
                from(ZONED_DATE_TIME.plusYears(YEARS_OF_CERTIFICATE_VALIDITY)),
                subject,
                info
        );
    }

    private static Date from(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    private static ContentSigner getContentSigner(KeyPair pair) {
        try {
            return new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
                    .build(pair.getPrivate());
        } catch (OperatorCreationException e) {
            throw new CertificateCreationException(CertificateCreationStep.GET_SIGNER, e);
        }
    }

    private static X509CertificateHolder getCertificateHolder(
            X509v3CertificateBuilder builder,
            SubjectPublicKeyInfo info,
            ContentSigner signer
    ) {
        var subjectPublicKeyId = new BcX509ExtensionUtils().createSubjectKeyIdentifier(info);
        try {
            return builder
                    .addExtension(Extension.basicConstraints, true, new BasicConstraints(true))
                    .addExtension(Extension.subjectKeyIdentifier, false, subjectPublicKeyId)
                    .build(signer);
        } catch (IOException e) {
            throw new CertificateCreationException(CertificateCreationStep.GET_HOLDER, e);
        }
    }

    private static X509Certificate newInstance(X509CertificateHolder holder) {
        try {
            return new JcaX509CertificateConverter()
                    .setProvider(Give.provider())
                    .getCertificate(holder);
        } catch (CertificateException e) {
            throw new CertificateCreationException(CertificateCreationStep.NEW_INSTANCE, e);
        }
    }
}
