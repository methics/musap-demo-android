package fi.methics.musap.sdk.keyuri;

public class MusapMessage {

    public String payload;
    public String type;
    public String uuid;
    public String transid;
    public String mac;
    public String iv;

    private transient boolean isError;
    private transient boolean isMt;
    private transient boolean isEncrypted;

}
