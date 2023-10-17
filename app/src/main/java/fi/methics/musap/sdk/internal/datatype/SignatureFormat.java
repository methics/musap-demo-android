package fi.methics.musap.sdk.internal.datatype;


public class SignatureFormat {

    public static final SignatureFormat CMS = new SignatureFormat("CMS");
    public static final SignatureFormat RAW = new SignatureFormat("RAW"); // a.k.a. PKCS1

    private String format;

    public SignatureFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

}
