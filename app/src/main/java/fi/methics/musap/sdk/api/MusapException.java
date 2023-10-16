package fi.methics.musap.sdk.api;

public class MusapException extends Exception {

    public MusapException(Exception cause) {
        super(cause);
    }

    public MusapException(String msg) {
        super(msg);
    }

}
