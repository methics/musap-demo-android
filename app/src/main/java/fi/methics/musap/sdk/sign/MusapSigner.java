package fi.methics.musap.sdk.sign;

import android.app.Activity;

import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.keyuri.MusapKey;
import fi.methics.musap.sdk.util.MLog;
import fi.methics.musap.sdk.util.MusapCallback;

public class MusapSigner {

    private final MusapKey key;
    private final Activity activity;

    public MusapSigner(MusapKey key, Activity activity) {
        this.key = key;
        this.activity = activity;
    }

    public void sign(byte[] data, MusapCallback<MusapSignature> callback) throws MusapException {
        if (this.key == null || data == null) {
            MLog.e("Missing key or data");
            throw new MusapException("Missing key or data");
        }

        if (this.key.getSscdType() == null) {
            MLog.e("Cannot sign. Missing SSCD type");
            throw new MusapException("Missing key type");
        }
        try {
            MusapSscdInterface sscd = this.key.getSscd();
            if (sscd == null) {
                throw new MusapException("No SSCD found for key " + this.key.getKeyUri());
            }
            MusapClient.sign(sscd, new SignatureReqBuilder().setKey(this.key).setData(data).setActivity(this.activity).createSignatureReq(), callback);
        } catch (Exception e) {
            throw new MusapException(e);
        }
    }
}
