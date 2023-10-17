package fi.methics.musap.sdk.internal.sign;

import android.app.Activity;

import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.MusapSignatureAlgorithm;

public class SignatureReq {

    protected MusapKey key;
    protected byte[]  data;
    protected MusapSignatureAlgorithm algorithm;
    protected Activity activity;

    public SignatureReq(MusapKey key, byte[] data, MusapSignatureAlgorithm algorithm, Activity activity) {
        this.key = key;
        this.data = data;
        this.algorithm = algorithm;
        this.activity = activity;
    }

    public MusapKey getKey() {
        return key;
    }

    public byte[] getData() {
        return data;
    }

    public MusapSignatureAlgorithm getAlgorithm() {
        return algorithm;
    }

    public Activity getActivity() {
        return activity;
    }
}
