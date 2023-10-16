package fi.methics.musap.sdk.internal.datatype;

import java.util.Objects;

/**
 * Key/Signature algorithm definition class
 */
public class MusapAlgorithm {

    // Note: Schemes according to SOG-IS ACM
    public static final String SCHEME_RSA_PSS  = "PSS (PKCS#1v2.1)";
    public static final String SCHEME_PKCS_1_5 = "PKCS#1v1.5";
    public static final String SCHEME_ECDSA    = "EC-DSA";

    public static final String PRIMITIVE_RSA = "RSA";
    public static final String PRIMITIVE_EC  = "EC";

    public static final MusapAlgorithm RSA_2K_PSS      = new MusapAlgorithm(SCHEME_RSA_PSS, PRIMITIVE_RSA, 2048);
    public static final MusapAlgorithm ECDSA_SECP256K1 = new MusapAlgorithm(SCHEME_ECDSA,   PRIMITIVE_EC, "secp256k1", 256);

    public String scheme;
    public String primitive;
    public String curve;
    public int key_length;

    public MusapAlgorithm(String scheme, String primitive, String curve, int keyLength) {
        this.scheme     = scheme;
        this.primitive  = primitive;
        this.curve      = curve;
        this.key_length = keyLength;
    }

    public MusapAlgorithm(String scheme, String primitive, int keyLength) {
        this.scheme     = scheme;
        this.primitive  = primitive;
        this.curve      = curve;
        this.key_length = keyLength;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusapAlgorithm that = (MusapAlgorithm) o;
        if (key_length != that.key_length) return false;
        if (!Objects.equals(scheme, that.scheme)) return false;
        if (!Objects.equals(primitive, that.primitive))  return false;
        return Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        int result = scheme != null ? scheme.hashCode() : 0;
        result = 31 * result + (primitive != null ? primitive.hashCode() : 0);
        result = 31 * result + (curve != null ? curve.hashCode() : 0);
        result = 31 * result + key_length;
        return result;
    }
}
