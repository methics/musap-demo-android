package fi.methics.musap.sdk.keyuri;

public class MUSAPPublicKey {

    private byte[] publickeyDer;

    public MUSAPPublicKey(byte[] publicKey) {
        this.publickeyDer = publicKey;
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
