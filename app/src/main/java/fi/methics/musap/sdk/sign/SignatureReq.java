package fi.methics.musap.sdk.sign;

import android.app.Activity;

import fi.methics.musap.sdk.keyuri.MUSAPKey;

public class SignatureReq {

    protected MUSAPKey key;
    protected byte[]  data;
    protected String algorithm;
    protected Activity activity;

    public SignatureReq(MUSAPKey key, byte[] data, String algorithm, Activity activity) {
        this.key = key;
        this.data = data;
        this.algorithm = algorithm;
        this.activity = activity;
    }

    public MUSAPKey getKey() {
        return key;
    }

    public byte[] getData() {
        return data;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Activity getActivity() {
        return activity;
    }
}
