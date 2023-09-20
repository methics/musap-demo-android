package fi.methics.musap.ui;

import android.app.Application;

import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryAPI;
import fi.methics.musap.sdk.sscd.AndroidKeystoreSscd;
import fi.methics.musap.sdk.sscd.MethicsDemoSscd;

public class MUSAPApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MUSAPClient.init(this);

        MUSAPClient.enableSSCD(new AndroidKeystoreSscd(this));
        MUSAPClient.enableSSCD(new MethicsDemoSscd(this));

    }
}
