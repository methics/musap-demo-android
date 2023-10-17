package fi.methics.musap.sdk.internal.keygeneration;

import android.app.Activity;
import android.view.View;

import fi.methics.musap.sdk.internal.datatype.MusapKeyAlgorithm;

public class KeyGenReq {

    protected KeyType type;
    protected String keyAlias;
    protected MusapKeyAlgorithm keyAlgorithm;
    protected Activity activity;
    protected View view;

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

    public View getView() {
        return view;
    }

    public MusapKeyAlgorithm getAlgorithm() {
        return this.keyAlgorithm;
    }

}
