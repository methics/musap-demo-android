package fi.methics.musap.sdk.sign;

import fi.methics.musap.sdk.util.MBase64;

public class MUSAPSignature {

    private byte[] rawSignature;

    public MUSAPSignature(byte[] rawSignature) {
        this.rawSignature = rawSignature;
    }

    public byte[] getRawSignature() {
        return this.rawSignature;
    }

    public String getB64Signature() {
        return MBase64.toBase64String(this.rawSignature);
    }

}
