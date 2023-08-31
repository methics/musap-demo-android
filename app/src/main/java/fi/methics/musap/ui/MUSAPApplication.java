package fi.methics.musap.ui;

import android.app.Application;

import fi.methics.musap.sdk.api.MUSAPClient;

public class MUSAPApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MUSAPClient.init(this);
    }
}
