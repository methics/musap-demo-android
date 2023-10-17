package fi.methics.musap.sdk.internal.datatype;

import fi.methics.musap.sdk.internal.util.MBase64;

public class MusapSignature {

    private byte[] rawSignature;
    private MusapKey key;
    private MusapSignatureAlgorithm algorithm;

    public MusapSignature(byte[] rawSignature) {
        this.rawSignature = rawSignature;
    }

    public MusapSignature(byte[] rawSignature, MusapKey key) {
        this.rawSignature = rawSignature;
        this.key          = key;
    }

    public MusapSignature(byte[] rawSignature, MusapKey key, MusapSignatureAlgorithm algorithm) {
        this.rawSignature = rawSignature;
        this.key          = key;
        this.algorithm    = algorithm;
    }

    /**
     * Get Signature Algorithm used to generate this signature
     * @return Signature Algorithm
     */
    public MusapSignatureAlgorithm getSignatureAlgorithm() {
        return this.algorithm;
    }

    /**
     * Get the key that was used to generate this signature
     * @return Key details
     */
    public MusapKey getKey() {
        return this.key;
    }

    public byte[] getRawSignature() {
        return this.rawSignature;
    }

    public String getB64Signature() {
        return MBase64.toBase64String(this.rawSignature);
    }

}
