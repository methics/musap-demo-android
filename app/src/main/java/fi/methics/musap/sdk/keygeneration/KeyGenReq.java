package fi.methics.musap.sdk.keygeneration;

import fi.methics.musap.sdk.api.MUSAPSscd;

public class KeyGenReq {

    private final KeyType type;

    private final MUSAPSscd sscd;

    private final String keyAlias;

    public KeyGenReq(KeyType type, MUSAPSscd sscd, String keyAlias) {
        this.type = type;
        this.sscd = sscd;
        this.keyAlias = keyAlias;
    }

    public KeyType getType() {
        return type;
    }

    public MUSAPSscd getSscd() {
        return sscd;
    }

    public String getKeyAlias() {
        return keyAlias;
    }
}
