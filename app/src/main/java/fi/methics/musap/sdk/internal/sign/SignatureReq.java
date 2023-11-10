package fi.methics.musap.sdk.internal.sign;

import android.app.Activity;

import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.SignatureAlgorithm;
import fi.methics.musap.sdk.internal.datatype.SignatureFormat;

/**
 * MUSAP Signature Request
 */
public class SignatureReq {

    protected MusapKey key;
    protected byte[]  data;
    protected SignatureAlgorithm algorithm;
    protected SignatureFormat format;
    protected Activity activity;

    public SignatureReq(MusapKey key, byte[] data, SignatureAlgorithm algorithm, SignatureFormat format) {
        this.key = key;
        this.data = data;
        this.algorithm = algorithm;
        this.format = format;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Get the public key reference of the key to use
     * @return key reference
     */
    public MusapKey getKey() {
        return key;
    }

    /**
     * Get the data to sign
     * @return data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Get the desired signature algorithm.
     * @return signature algorithm
     */
    public SignatureAlgorithm getAlgorithm() {
        if (this.algorithm == null && this.key != null) return this.key.getDefaultsignatureAlgorithm();
        return algorithm;
    }

    /**
     * Get the desired signature format. If not defined, {@link SignatureFormat#CMS} is used.
     * @return signature format
     */
    public SignatureFormat getFormat() {
        if (this.format == null) return SignatureFormat.RAW;
        return format;
    }

    public Activity getActivity() {
        return activity;
    }

    public static class Builder {
        private MusapKey key;
        private byte[] data;
        private SignatureAlgorithm algorithm;
        private SignatureFormat format;

        public Builder(SignatureAlgorithm algorithm) {
            this.algorithm = algorithm;
        }

        public Builder setKey(MusapKey key) {
            this.key = key;
            return this;
        }

        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder setFormat(SignatureFormat format) {
            this.format = format;
            return this;
        }

        public SignatureReq createSignatureReq() {
            return new SignatureReq(key, data, algorithm, format);
        }
    }

}
