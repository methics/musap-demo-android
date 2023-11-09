package fi.methics.musap.sdk.sscd.yubikey;

import android.app.Activity;

import java.util.concurrent.CompletableFuture;

import fi.methics.musap.sdk.internal.discovery.KeyBindReq;
import fi.methics.musap.sdk.internal.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.internal.sign.SignatureReq;
import fi.methics.musap.sdk.internal.util.KeyGenerationResult;
import fi.methics.musap.sdk.internal.util.SigningResult;

public class SscdTransaction {

    public SignatureReq sigReq;
    public KeyGenReq keyGenReq;
    public KeyBindReq keyBindReq;
    public CompletableFuture<KeyGenerationResult> keyGenFuture;
    public CompletableFuture<SigningResult> signFuture;
    public CompletableFuture<SigningResult> bindingFuture;

    public SscdTransaction(SignatureReq req, CompletableFuture<SigningResult> future) {
        this.sigReq     = req;
        this.signFuture = future;
    }

    public SscdTransaction(KeyGenReq req, CompletableFuture<KeyGenerationResult> future) {
        this.keyGenReq    = req;
        this.keyGenFuture = future;
    }

    public SscdTransaction(KeyBindReq req, CompletableFuture<SigningResult> future) {
        this.keyBindReq    = req;
        this.bindingFuture = future;
    }

    public Activity getActivity() {
        if (sigReq    != null) return sigReq.getActivity();
        if (keyGenReq != null) return keyGenReq.getActivity();
        return null;
    }

}
