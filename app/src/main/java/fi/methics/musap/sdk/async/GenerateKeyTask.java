package fi.methics.musap.sdk.async;

import android.content.Context;

import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.discovery.MetadataStorage;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;
import fi.methics.musap.sdk.util.AsyncTaskResult;
import fi.methics.musap.sdk.util.MusapAsyncTask;
import fi.methics.musap.sdk.util.MusapCallback;

public class GenerateKeyTask extends MusapAsyncTask<MUSAPKey> {

    private final MUSAPSscdInterface sscd;
    private final KeyGenReq req;

    public GenerateKeyTask(MusapCallback<MUSAPKey> callback, Context context, MUSAPSscdInterface sscd, KeyGenReq req) {
        super(callback, context);
        this.sscd = sscd;
        this.req  = req;
    }

    @Override
    protected AsyncTaskResult<MUSAPKey> runOperation() throws MUSAPException {
        try {
            MUSAPKey key = sscd.generateKey(req);
            MetadataStorage storage = new MetadataStorage(context.get());
            storage.storeKey(key, sscd.getSscdInfo());
            return new AsyncTaskResult<>(key);
        } catch (Exception e) {
            throw new MUSAPException(e);
        }
    }
}
