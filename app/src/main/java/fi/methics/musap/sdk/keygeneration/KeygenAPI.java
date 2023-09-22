package fi.methics.musap.sdk.keygeneration;

import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.util.MLog;

@Deprecated
public class KeygenAPI {

    public MUSAPKey generateKey(KeyGenReq req) {
        if (req == null) {
            MLog.e("Null keygen request");
            throw new IllegalArgumentException("Null keygen request");
        }

        //if (req.getSscd() == null || req.getSscd().getSscdType() == null) {
        //    MLog.e("Missing SSCD type");
        //    throw new IllegalArgumentException("Missing SSCD type");
        //}
//
        //try {
        //    switch (req.getSscd().getSscdType()) {
        //        case PHONE_KEYSTORE:
        //            MLog.d("Generating a key in Android keystore");
//
        //            MUSAPKey key = new AndroidKeyGenerator().generateKey(req);
        //            return key;
        //    }
        //} catch (Exception e) {
        //    MLog.e("Failed to generate key", e);
        //}

        return null;
    }
}
