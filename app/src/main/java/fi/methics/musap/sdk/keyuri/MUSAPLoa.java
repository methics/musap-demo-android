package fi.methics.musap.sdk.keyuri;

// TODO: This is duplicated by fi.methics.musap.util.LoA
public class MUSAPLoa {

    public static final String LOA_SCHEME_EIDAS = "EIDAS-2014";
    public static final String LOA_SCHEME_ISO   = "ISO-29115";

    public static final MUSAPLoa EIDAS_LOW         = new MUSAPLoa("low", LOA_SCHEME_EIDAS);
    public static final MUSAPLoa EIDAS_SUBSTANTIAL = new MUSAPLoa("substantial", LOA_SCHEME_EIDAS);
    public static final MUSAPLoa EIDAS_HIGH        = new MUSAPLoa("high", LOA_SCHEME_EIDAS);
    public static final MUSAPLoa ISO_LOA1         = new MUSAPLoa("loa1", LOA_SCHEME_ISO);
    public static final MUSAPLoa ISO_LOA2         = new MUSAPLoa("loa2", LOA_SCHEME_ISO);
    public static final MUSAPLoa ISO_LOA3         = new MUSAPLoa("loa3", LOA_SCHEME_ISO);
    public static final MUSAPLoa ISO_LOA4         = new MUSAPLoa("loa4", LOA_SCHEME_ISO);

    private String loa;
    private String scheme;

    public MUSAPLoa(String loa, String scheme) {
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
