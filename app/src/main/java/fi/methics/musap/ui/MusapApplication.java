package fi.methics.musap.ui;

import android.app.Application;

import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.sscd.android.AndroidKeystoreSscd;
import fi.methics.musap.sdk.sscd.methicsdemo.MethicsDemoSscd;
import fi.methics.musap.sdk.sscd.yubikey.YubiKeyExtension;

public class MusapApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MusapClient.init(this);

        MusapClient.enableSSCD(new AndroidKeystoreSscd(this));
        MusapClient.enableSSCD(new MethicsDemoSscd(this));
        MusapClient.enableSSCD(new YubiKeyExtension(this));

    }
}
