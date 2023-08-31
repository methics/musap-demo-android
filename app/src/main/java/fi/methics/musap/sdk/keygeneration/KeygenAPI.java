package fi.methics.musap.sdk.keygeneration;

import fi.methics.musap.sdk.util.MLog;

public class KeygenAPI {

    public void generateKey(KeyGenReq req) {
        if (req == null) {
            MLog.d("Null keygen request");
            return;
        }

        if (req.getSscd() == null || req.getSscd().getSscdType() == null) {
            MLog.d("Missing SSCD type");
            return;
        }

        try {
            switch (req.getSscd().getSscdType()) {
                case PHONE_KEYSTORE:
                    new AndroidKeyGenerator().generateKey(req);
                    break;
            }
        } catch (Exception e) {
            MLog.e("Failed to generate key", e);
        }

    }
}
