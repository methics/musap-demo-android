package fi.methics.musap.sdk.sign;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import fi.methics.musap.sdk.api.MBase64;
import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.keyuri.MUSAPCertificate;
import fi.methics.musap.sdk.keyuri.MUSAPKey;

public class CMSSignature extends MUSAPSignature {

    private CMSSignedData signedData;

    public CMSSignature(byte[] cms) throws MUSAPException {
        super(cms);
        try {
            final ContentInfo ci = ContentInfo.getInstance(cms);
            this.signedData = new CMSSignedData(ci);
        } catch (Exception e) {
            throw new MUSAPException(e);
        }
    }

    /**
     * Get the CMS signed data object
     * @return CMS signed data
     */
    public CMSSignedData getSignedData() {
        return this.signedData;
    }

    /**
     * Get the X509 Certificates related to this CMS signature
     * @return Collection of certificates
     * @throws IOException
     */
    public Collection<X509CertificateHolder> getCertificates() throws IOException {
        return signedData.getCertificates().getMatches(null);
    }

    /**
     * Get the signer's X509 certificate
     * @return certificate
     * @throws IOException
     */
    public MUSAPCertificate getSignerCertificate() throws IOException {
        Collection<X509CertificateHolder> certs = this.getCertificates();
        X509CertificateHolder cert = certs.stream().findFirst().orElse(null);
        return new MUSAPCertificate(cert);
    }

    /**
     * Get the key related to signer's certificate
     * @return signer key
     * @throws IOException
     */
    public MUSAPKey getSignerKey() throws IOException {
        MUSAPKey.Builder builder = new MUSAPKey.Builder();
        builder.setCertificate(this.getSignerCertificate());
        return builder.build();
    }

}
