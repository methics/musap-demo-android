package fi.methics.musap.sdk.util;

import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.sign.MUSAPSignature;

public class SigningResult {

    public MUSAPSignature signature;
    public MUSAPException exception;

    public SigningResult(MUSAPSignature signature) {
        this.signature = signature;
    }

    public SigningResult(MUSAPException e) {
        this.exception = e;
    }

}