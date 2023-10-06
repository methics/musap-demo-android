package fi.methics.musap.ui;

import android.app.Application;

import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.sscd.android.AndroidKeystoreSscd;
import fi.methics.musap.sdk.sscd.methicsdemo.MethicsDemoSscd;
import fi.methics.musap.sdk.yubikey.YubiKeyExtension;

public class MUSAPApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MUSAPClient.init(this);

        MUSAPClient.enableSSCD(new AndroidKeystoreSscd(this));
        MUSAPClient.enableSSCD(new MethicsDemoSscd(this));
        MUSAPClient.enableSSCD(new YubiKeyExtension(this));

    }
}
