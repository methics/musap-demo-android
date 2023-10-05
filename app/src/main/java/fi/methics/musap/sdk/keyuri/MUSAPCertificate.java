package fi.methics.musap.sdk.keyuri;

import org.bouncycastle.cert.X509CertificateHolder;

import java.io.IOException;

public class MUSAPCertificate {

    private String subject;
    private byte[] certificate;
    private MUSAPPublicKey publicKey;

    public MUSAPCertificate(String subject, byte[] cert) {
        this.subject     = subject;
        this.certificate = cert;
    }

    public MUSAPCertificate(X509CertificateHolder cert) throws IOException {
        this.subject     = cert.getSubject().toString();
        this.certificate = cert.getEncoded();
        this.publicKey   = new MUSAPPublicKey(cert.getSubjectPublicKeyInfo().getEncoded());
    }

    public String getSubject() {
        return this.subject;
    }

    public byte[] getCertificate() {
        return this.certificate;
    }

    public MUSAPPublicKey getPublicKey() {
        return this.publicKey;
    }

}
