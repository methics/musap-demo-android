/*
 * (c) Copyright 2003-2020 Methics Oy. All rights reserved.
 */

package fi.methics.musap.sdk.util;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import fi.methics.musap.sdk.api.MUSAPException;

public abstract class MusapAsyncTask<T> extends AsyncTask<Void, Void, AsyncTaskResult<T>> {

    private final MusapCallback<T> callback;
    protected final WeakReference<Context> context;

    public MusapAsyncTask(MusapCallback<T> callback, Context context) {
        this.callback = callback;
        this.context = new WeakReference<>(context);
    }

    @Override
    protected final AsyncTaskResult<T> doInBackground(Void... v) {
        AsyncTaskResult<T> asyncTaskResult;
        try {
            asyncTaskResult = this.runOperation();
        } catch (MUSAPException e) {
            asyncTaskResult = new AsyncTaskResult<>(e);
        } catch (Exception e) {
            asyncTaskResult = new AsyncTaskResult<>(new MUSAPException(e));
        }

        return asyncTaskResult;
    }

    @Override
    protected final void onPostExecute(AsyncTaskResult<T> result) {
        // Sometimes the caller does not need a callback
        if (this.callback == null) {
            return;
        }

        if (result.getError() != null) {
            if (result.getError() instanceof MUSAPException) {
                this.callback.onException((MUSAPException) result.getError());
            } else {
                this.callback.onException(new MUSAPException(result.getError()));
            }
        } else {
            this.callback.onSuccess(result.getResult());
        }
    }


    protected abstract AsyncTaskResult<T> runOperation() throws MUSAPException;

}
