package fi.methics.musap.sdk.internal.datatype;

import fi.methics.musap.sdk.internal.util.MBase64;

/**
 * MUSAP Signature class. This contains the raw signature and various signature details like
 * algorithm, format and usually a public key reference.
 */
public class MusapSignature {

    private byte[] rawSignature;
    private MusapKey key;
    private MusapSignatureAlgorithm algorithm;
    private MusapSignatureFormat format;

    /**
     * Create a new MUSAP Signature object
     * @param rawSignature Raw signature byte[]
     * @param key          Public key reference
     * @param algorithm    Signature algorithm
     * @param format       Signature format
     */
    public MusapSignature(byte[] rawSignature,
                          MusapKey key,
                          MusapSignatureAlgorithm algorithm,
                          MusapSignatureFormat format)
    {
        this.rawSignature = rawSignature;
        this.key          = key;
        this.algorithm    = algorithm;
        this.format       = format;
    }

    /**
     * Create a new raw signature without any meta-data
     * @param rawSignature Raw signature byte[]
     */
    public MusapSignature(byte[] rawSignature) {
        this.rawSignature = rawSignature;
    }

    /**
     * Create a new raw signature with a public key reference
     * @param rawSignature Raw signature byte[]
     * @param key          Public key reference
     */
    public MusapSignature(byte[] rawSignature, MusapKey key) {
        this.rawSignature = rawSignature;
        this.key          = key;
    }

    /**
     * Get Signature Algorithm used to generate this signature
     * @return Signature Algorithm
     */
    public MusapSignatureAlgorithm getSignatureAlgorithm() {
        return this.algorithm;
    }

    /**
     * Get the signature format
     * @return format
     */
    public MusapSignatureFormat getSignatureFormat() {
        return this.format;
    }

    /**
     * Get the key that was used to generate this signature
     * @return Key details
     */
    public MusapKey getKey() {
        return this.key;
    }

    /**
     * Get the signature bytes
     * @return signature bytes
     */
    public byte[] getRawSignature() {
        return this.rawSignature;
    }

    /**
     * Get the signature as a Base64 String
     * @return signature Base64
     */
    public String getB64Signature() {
        return MBase64.toBase64String(this.rawSignature);
    }

}
