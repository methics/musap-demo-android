package fi.methics.musap.sdk.keygeneration;

import fi.methics.musap.sdk.api.MUSAPSscd;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;

public class KeyGenReq {

    private final KeyType type;
    private final String keyAlias;

    public KeyGenReq(KeyType type, MUSAPSscd sscd, String keyAlias) {
        this.type = type;
        this.keyAlias = keyAlias;
    }

    public KeyType getType() {
        return type;
    }

    public String getKeyAlias() {
        return keyAlias;
    }
}
