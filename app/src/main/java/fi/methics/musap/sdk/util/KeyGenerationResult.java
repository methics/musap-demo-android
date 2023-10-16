package fi.methics.musap.sdk.util;

import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.keyuri.MusapKey;

public class KeyGenerationResult {

    public MusapKey key;
    public MusapException exception;

    public KeyGenerationResult(MusapKey key) {
        this.key = key;
    }

    public KeyGenerationResult(MusapException e) {
        this.exception = e;
    }

}