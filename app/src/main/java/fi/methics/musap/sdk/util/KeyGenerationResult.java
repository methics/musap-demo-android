package fi.methics.musap.sdk.util;

import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.keyuri.MUSAPKey;

public class KeyGenerationResult {

    public MUSAPKey key;
    public MUSAPException exception;

    public KeyGenerationResult(MUSAPKey key) {
        this.key = key;
    }

    public KeyGenerationResult(MUSAPException e) {
        this.exception = e;
    }

}