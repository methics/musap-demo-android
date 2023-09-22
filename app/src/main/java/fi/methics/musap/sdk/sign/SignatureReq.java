package fi.methics.musap.sdk.sign;

import fi.methics.musap.sdk.keyuri.MUSAPKey;

public class SignatureReq {

    private MUSAPKey key;
    private byte[]  data;
    private String algorithm;

    public SignatureReq(MUSAPKey key, byte[] data) {
        this.data = data;
        this.key  = key;
    }

    public byte[] getData() {
        return this.data;
    }

    public MUSAPKey getKey() {
        return this.key;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

}
