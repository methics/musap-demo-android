package fi.methics.musap.sdk.async;

import android.content.Context;

import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.sign.MusapSignature;
import fi.methics.musap.sdk.sign.SignatureReq;
import fi.methics.musap.sdk.util.AsyncTaskResult;
import fi.methics.musap.sdk.util.MusapAsyncTask;
import fi.methics.musap.sdk.util.MusapCallback;

public class SignTask extends MusapAsyncTask<MusapSignature> {

    private final MusapSscdInterface sscd;
    private final SignatureReq req;

    public SignTask(MusapCallback<MusapSignature> callback, Context context, MusapSscdInterface sscd, SignatureReq req) {
        super(callback, context);
        this.sscd = sscd;
        this.req  = req;
    }

    @Override
    protected AsyncTaskResult<MusapSignature> runOperation() throws MusapException {
        try {
            MusapSignature signature = sscd.sign(req);
            return new AsyncTaskResult<>(signature);
        } catch (Exception e) {
            throw new MusapException(e);
        }
    }
}
