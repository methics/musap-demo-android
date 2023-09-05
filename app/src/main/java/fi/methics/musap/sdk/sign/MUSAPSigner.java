package fi.methics.musap.sdk.sign;

import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.util.MLog;

public class MUSAPSigner {

    private final MUSAPKey key;

    public MUSAPSigner(MUSAPKey key) {
        this.key = key;
    }

    public byte[] sign(byte[] data) throws MUSAPException {
        if (this.key == null || data == null) {
            MLog.e("Missing key or data");
            throw new IllegalArgumentException("Missing key or data");
        }

        if (this.key.getSscdType() == null) {
            MLog.e("Cannot sign. Missing SSCD type");
            throw new IllegalArgumentException("Missing key type");
        }
        try {
            switch (this.key.getSscdType().toLowerCase()) {
                case "aks":
                case "android keystore":
                    return new AksSigner().sign(this.key, data);
            }
        } catch (Exception e) {
            throw new MUSAPException(e);
        }

        // Unknown signing method
        throw new IllegalArgumentException("Cannot handle SSCD type " + this.key.getSscdType());
    }
}
