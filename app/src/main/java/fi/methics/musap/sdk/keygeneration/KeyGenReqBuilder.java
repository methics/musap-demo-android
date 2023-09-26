package fi.methics.musap.sdk.keygeneration;

import android.app.Activity;

import fi.methics.musap.sdk.api.MUSAPSscd;

public class KeyGenReqBuilder {
    private KeyType type;
    private String keyAlias;
    private Activity activity;
    private MUSAPSscd sscd;

    public KeyGenReqBuilder setType(KeyType type) {
        this.type = type;
        return this;
    }

    @Deprecated
    public KeyGenReqBuilder setSscd(MUSAPSscd sscd) {
        this.sscd = sscd;
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

    public KeyGenReq createKeyGenReq() {
        KeyGenReq req = new KeyGenReq();
        req.type     = type;
        req.keyAlias = keyAlias;
        req.activity = activity;
        return req;
    }
}