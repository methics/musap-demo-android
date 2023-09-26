package fi.methics.musap.sdk.keygeneration;

import android.app.Activity;

import fi.methics.musap.sdk.api.MUSAPSscd;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;

public class KeyGenReq {

    protected KeyType type;
    protected String keyAlias;
    protected Activity activity;

    protected KeyGenReq() {

    }

    public KeyType getType() {
        return type;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public Activity getActivity() {
        return activity;
    }
}
