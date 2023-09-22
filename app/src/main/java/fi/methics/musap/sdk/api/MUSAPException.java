package fi.methics.musap.sdk.api;

public class MUSAPException extends Exception {

    public MUSAPException(Exception cause) {
        super(cause);
    }

    public MUSAPException(String msg) {
        super(msg);
    }

}
