package fi.methics.musap.sdk.internal.sign;

import android.app.Activity;

import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.MusapSignatureAlgorithm;
import fi.methics.musap.sdk.internal.datatype.MusapSignatureFormat;

/**
 * MUSAP Signature Request
 */
public class SignatureReq {

    protected MusapKey key;
    protected byte[]  data;
    protected MusapSignatureAlgorithm algorithm;
    protected MusapSignatureFormat format;
    protected Activity activity;

    public SignatureReq(MusapKey key, byte[] data, MusapSignatureAlgorithm algorithm, MusapSignatureFormat format, Activity activity) {
        this.key = key;
        this.data = data;
        this.algorithm = algorithm;
        this.format = format;
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
    public MusapSignatureAlgorithm getAlgorithm() {
        if (this.algorithm == null && this.key != null) return this.key.getDefaultsignatureAlgorithm();
        return algorithm;
    }

    /**
     * Get the desired signature format. If not defined, {@link MusapSignatureFormat#CMS} is used.
     * @return signature format
     */
    public MusapSignatureFormat getFormat() {
        if (this.format == null) return MusapSignatureFormat.RAW;
        return format;
    }

    public Activity getActivity() {
        return activity;
    }
}
