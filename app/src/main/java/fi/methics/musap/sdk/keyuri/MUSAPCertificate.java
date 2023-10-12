package fi.methics.musap.sdk.keyuri;

import org.bouncycastle.cert.X509CertificateHolder;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class MUSAPCertificate {

    private String subject;
    private byte[] certificate;
    private MUSAPPublicKey publicKey;

    public MUSAPCertificate(String subject, byte[] cert, MUSAPPublicKey publicKey) {
        this.subject     = subject;
        this.certificate = cert;
        this.publicKey   = publicKey;
    }

    public MUSAPCertificate(X509CertificateHolder cert) throws IOException {
        this.subject     = cert.getSubject().toString();
        this.certificate = cert.getEncoded();
        this.publicKey   = new MUSAPPublicKey(cert.getSubjectPublicKeyInfo().getEncoded());
    }

    public MUSAPCertificate(X509Certificate cert) throws CertificateEncodingException {
        this.subject     = cert.getSubjectDN().toString();
        this.certificate = cert.getEncoded();
        this.publicKey   = new MUSAPPublicKey(cert.getPublicKey().getEncoded());
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
