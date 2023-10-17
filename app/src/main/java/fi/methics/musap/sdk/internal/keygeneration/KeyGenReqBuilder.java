package fi.methics.musap.sdk.internal.keygeneration;

import android.app.Activity;
import android.view.View;

import fi.methics.musap.sdk.internal.datatype.MusapKeyAlgorithm;

public class KeyGenReqBuilder {
    private KeyType type;
    private String keyAlias;
    protected MusapKeyAlgorithm keyAlgorithm;
    private Activity activity;
    private View view;

    public KeyGenReqBuilder setType(KeyType type) {
        this.type = type;
        return this;
    }

    public KeyGenReqBuilder setAlias(String alias) {
        this.keyAlias = alias;
        return this;
    }

    public KeyGenReqBuilder setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public KeyGenReqBuilder setKeyAlgorithm(MusapKeyAlgorithm keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
        return this;
    }

    public KeyGenReqBuilder setView(View view) {
        this.view = view;
        return this;
    }

    public KeyGenReq createKeyGenReq() {
        KeyGenReq req = new KeyGenReq();
        req.type     = type;
        req.keyAlias = keyAlias;
        req.activity = activity;
        req.view     = view;
        return req;
    }

}