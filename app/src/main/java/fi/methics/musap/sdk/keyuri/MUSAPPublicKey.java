package fi.methics.musap.sdk.keyuri;

import java.security.KeyPair;

public class MUSAPPublicKey {

    private byte[] publickeyDer;

    public MUSAPPublicKey(byte[] publicKey) {
        this.publickeyDer = publicKey;
    }

    public MUSAPPublicKey(KeyPair keyPair) {
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
