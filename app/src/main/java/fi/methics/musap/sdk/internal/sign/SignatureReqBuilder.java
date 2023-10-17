package fi.methics.musap.sdk.internal.sign;

import android.app.Activity;

import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.MusapSignatureAlgorithm;

public class SignatureReqBuilder {
    private MusapKey key;
    private byte[] data;
    private MusapSignatureAlgorithm algorithm;
    private Activity activity;

    public SignatureReqBuilder setKey(MusapKey key) {
        this.key = key;
        return this;
    }

    public SignatureReqBuilder setData(byte[] data) {
        this.data = data;
        return this;
    }

    public SignatureReqBuilder setAlgorithm(MusapSignatureAlgorithm algorithm) {
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