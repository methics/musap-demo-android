package fi.methics.musap.sdk.async;

import android.content.Context;

import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.discovery.MetadataStorage;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MusapKey;
import fi.methics.musap.sdk.util.AsyncTaskResult;
import fi.methics.musap.sdk.util.MLog;
import fi.methics.musap.sdk.util.MusapAsyncTask;
import fi.methics.musap.sdk.util.MusapCallback;

public class GenerateKeyTask extends MusapAsyncTask<MusapKey> {

    private final MusapSscdInterface sscd;
    private final KeyGenReq req;

    public GenerateKeyTask(MusapCallback<MusapKey> callback, Context context, MusapSscdInterface sscd, KeyGenReq req) {
        super(callback, context);
        this.sscd = sscd;
        this.req  = req;
    }

    @Override
    protected AsyncTaskResult<MusapKey> runOperation() throws MusapException {
        try {
            MusapKey key = sscd.generateKey(req);
            MLog.d("GenerateKeyTask Got MUSAP key");
            MetadataStorage storage = new MetadataStorage(context.get());
            storage.storeKey(key, sscd.getSscdInfo());
            return new AsyncTaskResult<>(key);
        } catch (Exception e) {
            throw new MusapException(e);
        }
    }
}
