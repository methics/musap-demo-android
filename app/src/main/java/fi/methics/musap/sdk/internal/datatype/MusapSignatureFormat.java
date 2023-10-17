package fi.methics.musap.sdk.internal.datatype;


public class MusapSignatureFormat {

    public static final MusapSignatureFormat CMS = new MusapSignatureFormat("CMS");
    public static final MusapSignatureFormat RAW = new MusapSignatureFormat("RAW"); // a.k.a. PKCS1

    private String format;

    public MusapSignatureFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

}
