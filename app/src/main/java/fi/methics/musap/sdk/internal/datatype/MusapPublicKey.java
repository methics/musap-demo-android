package fi.methics.musap.sdk.internal.datatype;

import java.security.KeyPair;

public class MusapPublicKey {

    private byte[] publickeyDer;

    public MusapPublicKey(byte[] publicKey) {
        this.publickeyDer = publicKey;
    }

    public MusapPublicKey(KeyPair keyPair) {
        this.publickeyDer = keyPair.getPublic().getEncoded();
    }

    public byte[] getDER() {
        return this.publickeyDer;
    }

    public String getPEM() {
        return ""; // TODO;
    }

    public String getJWK() {
        return ""; // TODO;
    }

}
