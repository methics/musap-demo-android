package fi.methics.musap.sdk.async;

import android.content.Context;

import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.discovery.MetadataStorage;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.sign.MUSAPSignature;
import fi.methics.musap.sdk.sign.SignatureReq;
import fi.methics.musap.sdk.util.AsyncTaskResult;
import fi.methics.musap.sdk.util.MusapAsyncTask;
import fi.methics.musap.sdk.util.MusapCallback;

public class SignTask extends MusapAsyncTask<MUSAPSignature> {

    private final MUSAPSscdInterface sscd;
    private final SignatureReq req;

    public SignTask(MusapCallback<MUSAPSignature> callback, Context context, MUSAPSscdInterface sscd, SignatureReq req) {
        super(callback, context);
        this.sscd = sscd;
        this.req  = req;
    }

    @Override
    protected AsyncTaskResult<MUSAPSignature> runOperation() throws MUSAPException {
        try {
            MUSAPSignature signature = sscd.sign(req);
            return new AsyncTaskResult<>(signature);
        } catch (Exception e) {
            throw new MUSAPException(e);
        }
    }
}
