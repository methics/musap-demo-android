package fi.methics.musap.sdk.sign;

import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.util.MLog;

public class MUSAPSigner {

    private final MUSAPKey key;

    public MUSAPSigner(MUSAPKey key) {
        this.key = key;
    }

    public MUSAPSignature sign(byte[] data) throws MUSAPException {
        if (this.key == null || data == null) {
            MLog.e("Missing key or data");
            throw new IllegalArgumentException("Missing key or data");
        }

        if (this.key.getSscdType() == null) {
            MLog.e("Cannot sign. Missing SSCD type");
            throw new IllegalArgumentException("Missing key type");
        }
        try {
            MUSAPSscdInterface sscd = this.key.getSscd();
            if (sscd == null) {
                throw new MUSAPException("No SSCD found for key " + this.key.getKeyUri());
            }
            return sscd.sign(new SignatureReq(this.key, data));
        } catch (Exception e) {
            throw new MUSAPException(e);
        }
    }
}
