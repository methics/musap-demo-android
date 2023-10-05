package fi.methics.musap.sdk.discovery;

public class KeyBindReqBuilder {
    private String sscd;
    private String msisdn;

    private boolean generateNewKey;

    public KeyBindReqBuilder setSscd(String sscd) {
        this.sscd = sscd;
        return this;
    }

    public KeyBindReqBuilder setMsisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public KeyBindReqBuilder setGenerateNewKey(boolean generateNewKey) {
        this.generateNewKey = generateNewKey;
        return this;
    }


    public KeyBindReq createKeyBindReq() {
        return new KeyBindReq(sscd, msisdn, generateNewKey);
    }
}