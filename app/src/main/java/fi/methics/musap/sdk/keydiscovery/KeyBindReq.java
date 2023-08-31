package fi.methics.musap.sdk.keydiscovery;

public class KeyBindReq {

    private final String sscd;
    private final String msisdn;

    private final boolean generateNewKey;

    protected KeyBindReq(String sscd, String msisdn, boolean generateNewKey) {
        this.sscd = sscd;
        this.msisdn = msisdn;
        this.generateNewKey = generateNewKey;
    }

    public String getSscd() {
        return sscd;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public boolean isGenerateNewKey() {
        return generateNewKey;
    }
}
