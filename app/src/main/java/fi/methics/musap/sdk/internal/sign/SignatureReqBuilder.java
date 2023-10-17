package fi.methics.musap.sdk.internal.sign;

import android.app.Activity;

import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.SignatureAlgorithm;
import fi.methics.musap.sdk.internal.datatype.SignatureFormat;

public class SignatureReqBuilder {
    private MusapKey key;
    private byte[] data;
    private SignatureAlgorithm algorithm;
    private SignatureFormat format;
    private Activity activity;

    public SignatureReqBuilder setKey(MusapKey key) {
        this.key = key;
        return this;
    }

    /**
     * Set the data to be signed and signature algorithm
     * @param data      Data to be signed
     * @param algorithm Signature algorithm to use
     * @return this builder
     */
    public SignatureReqBuilder setData(byte[] data, SignatureAlgorithm algorithm) {
        this.data      = data;
        this.algorithm = algorithm;
        return this;
    }

    public SignatureReqBuilder setAlgorithm(SignatureAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public SignatureReqBuilder setFormat(SignatureFormat algorithm) {
        this.format = format;
        return this;
    }

    public SignatureReqBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public SignatureReq createSignatureReq() {
        return new SignatureReq(key, data, algorithm, format, activity);
    }
}