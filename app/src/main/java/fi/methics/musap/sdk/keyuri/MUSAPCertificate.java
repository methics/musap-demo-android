package fi.methics.musap.sdk.keyuri;

public class MUSAPCertificate {

    private String subject;
    private byte[] certificate;

    public MUSAPCertificate(String subject, byte[] cert) {
        this.subject     = subject;
        this.certificate = cert;
    }

    public String getSubject() {
        return this.subject;
    }

    public byte[] getCertificate() {
        return this.certificate;
    }

}
