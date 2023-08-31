package fi.methics.musap.sdk.keygeneration;

import fi.methics.musap.sdk.api.MUSAPSscd;

public class KeyGenReqBuilder {
    private KeyType type;
    private MUSAPSscd sscd;

    private String keyAlias;

    public KeyGenReqBuilder setType(KeyType type) {
        this.type = type;
        return this;
    }

    public KeyGenReqBuilder setSscd(MUSAPSscd sscd) {
        this.sscd = sscd;
        return this;
    }

    public KeyGenReqBuilder setAlias(String alias) {
        this.keyAlias = alias;
        return this;
    }

    public KeyGenReq createKeyGenReq() {
        return new KeyGenReq(type, sscd, this.keyAlias);
    }
}