package fi.methics.musap.sdk.sign;

import android.app.Activity;

import fi.methics.musap.sdk.keyuri.MUSAPKey;

public class SignatureReqBuilder {
    private MUSAPKey key;
    private byte[] data;
    private String algorithm;
    private Activity activity;

    public SignatureReqBuilder setKey(MUSAPKey key) {
        this.key = key;
        return this;
    }

    public SignatureReqBuilder setData(byte[] data) {
        this.data = data;
        return this;
    }

    public SignatureReqBuilder setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public SignatureReqBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public SignatureReq createSignatureReq() {
        return new SignatureReq(key, data, algorithm, activity);
    }
}