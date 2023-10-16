package fi.methics.musap.sdk.internal.sign;

import android.app.Activity;

import fi.methics.musap.sdk.internal.datatype.MusapKey;

public class SignatureReqBuilder {
    private MusapKey key;
    private byte[] data;
    private String algorithm;
    private Activity activity;

    public SignatureReqBuilder setKey(MusapKey key) {
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