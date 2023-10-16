package fi.methics.musap.sdk.internal.sign;

import fi.methics.musap.sdk.internal.util.MBase64;

public class MusapSignature {

    private byte[] rawSignature;

    public MusapSignature(byte[] rawSignature) {
        this.rawSignature = rawSignature;
    }

    public byte[] getRawSignature() {
        return this.rawSignature;
    }

    public String getB64Signature() {
        return MBase64.toBase64String(this.rawSignature);
    }

}
