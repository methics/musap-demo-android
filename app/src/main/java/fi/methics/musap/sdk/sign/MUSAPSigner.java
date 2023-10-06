package fi.methics.musap.sdk.sign;

import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.util.MLog;
import fi.methics.musap.sdk.util.MusapCallback;

public class MUSAPSigner {

    private final MUSAPKey key;

    public MUSAPSigner(MUSAPKey key) {
        this.key = key;
    }

    public void sign(byte[] data, MusapCallback<MUSAPSignature> callback) throws MUSAPException {
        if (this.key == null || data == null) {
            MLog.e("Missing key or data");
            throw new MUSAPException("Missing key or data");
        }

        if (this.key.getSscdType() == null) {
            MLog.e("Cannot sign. Missing SSCD type");
            throw new MUSAPException("Missing key type");
        }
        try {
            MUSAPSscdInterface sscd = this.key.getSscd();
            if (sscd == null) {
                throw new MUSAPException("No SSCD found for key " + this.key.getKeyUri());
            }
            MUSAPClient.sign(sscd, new SignatureReqBuilder().setKey(this.key).setData(data).createSignatureReq(), callback);
        } catch (Exception e) {
            throw new MUSAPException(e);
        }
    }
}
