package fi.methics.musap.sdk.util;

import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.sign.MusapSignature;

public class SigningResult {

    public MusapSignature signature;
    public MusapException exception;

    public SigningResult(MusapSignature signature) {
        this.signature = signature;
    }

    public SigningResult(MusapException e) {
        this.exception = e;
    }

}