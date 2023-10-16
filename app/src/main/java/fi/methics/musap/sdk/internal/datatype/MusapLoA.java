package fi.methics.musap.sdk.internal.datatype;

// TODO: This is duplicated by fi.methics.musap.util.LoA
public class MusapLoA {

    public static final String LOA_SCHEME_EIDAS = "EIDAS-2014";
    public static final String LOA_SCHEME_ISO   = "ISO-29115";

    public static final MusapLoA EIDAS_LOW         = new MusapLoA("low", LOA_SCHEME_EIDAS);
    public static final MusapLoA EIDAS_SUBSTANTIAL = new MusapLoA("substantial", LOA_SCHEME_EIDAS);
    public static final MusapLoA EIDAS_HIGH        = new MusapLoA("high", LOA_SCHEME_EIDAS);
    public static final MusapLoA ISO_LOA1         = new MusapLoA("loa1", LOA_SCHEME_ISO);
    public static final MusapLoA ISO_LOA2         = new MusapLoA("loa2", LOA_SCHEME_ISO);
    public static final MusapLoA ISO_LOA3         = new MusapLoA("loa3", LOA_SCHEME_ISO);
    public static final MusapLoA ISO_LOA4         = new MusapLoA("loa4", LOA_SCHEME_ISO);

    private String loa;
    private String scheme;

    public MusapLoA(String loa, String scheme) {
        this.loa    = loa;
        this.scheme = scheme;
    }

    public String getLoa() {
        return this.loa;
    }

    public String getScheme() {
        return this.scheme;
    }

}
