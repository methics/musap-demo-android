package fi.methics.musap.sdk.internal.datatype;

import java.util.Objects;

/**
 * Key algorithm definition class
 */
public class MusapKeyAlgorithm {

    public static final String PRIMITIVE_RSA = "RSA";
    public static final String PRIMITIVE_EC  = "EC";

    public static final MusapKeyAlgorithm RSA_2K      = new MusapKeyAlgorithm(PRIMITIVE_RSA, 2048);
    public static final MusapKeyAlgorithm RSA_4K      = new MusapKeyAlgorithm(PRIMITIVE_RSA, 4096);
    public static final MusapKeyAlgorithm ECC_P256_K1 = new MusapKeyAlgorithm(PRIMITIVE_EC, "secp256k1", 256);
    public static final MusapKeyAlgorithm ECC_P384_K1 = new MusapKeyAlgorithm(PRIMITIVE_EC, "secp384k1", 384);
    public static final MusapKeyAlgorithm ECC_P256_R1 = new MusapKeyAlgorithm(PRIMITIVE_EC, "secp256r1", 256);
    public static final MusapKeyAlgorithm ECC_P384_R1 = new MusapKeyAlgorithm(PRIMITIVE_EC, "secp384r1", 384);

    public String primitive;
    public String curve;
    public int key_length;

    /**
     * Create a new Key Algorithm
     * @param primitive Primitive (usually RSA or EC)
     * @param keyLength Key length
     */
    public MusapKeyAlgorithm(String primitive, int keyLength) {
        this.primitive  = primitive;
        this.key_length = keyLength;
    }

    /**
     * Create a new Key Algorithm with a specific curve
     * @param primitive Primitive (usually RSA or EC)
     * @param curve     Curve (e.g. secp256r1)
     * @param keyLength Key length
     */
    public MusapKeyAlgorithm(String primitive, String curve, int keyLength) {
        this.primitive  = primitive;
        this.curve      = curve;
        this.key_length = keyLength;
    }

    /**
     * Is this an RSA key?
     * @return true for RSA key
     */
    public boolean isRsa() {
        return PRIMITIVE_RSA.equals(this.primitive);
    }

    /**
     * Is this an EC key?
     * @return true for EC key
     */
    public boolean isEc() {
        return PRIMITIVE_EC.equals(this.primitive);
    }

    @Override
    public String toString() {
        if (curve != null) {
            return "[" + primitive + "/" + curve + "/"  + key_length + "]";
        } else {
            return "[" + primitive + "/"  + key_length + "]";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusapKeyAlgorithm that = (MusapKeyAlgorithm) o;
        if (key_length != that.key_length) return false;
        if (!Objects.equals(primitive, that.primitive))  return false;
        return Objects.equals(curve, that.curve);
    }

    @Override
    public int hashCode() {
        int result = primitive != null ? primitive.hashCode() : 0;
        result = 31 * result + (curve != null ? curve.hashCode() : 0);
        result = 31 * result + key_length;
        return result;
    }
}
