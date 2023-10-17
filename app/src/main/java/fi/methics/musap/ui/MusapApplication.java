package fi.methics.musap.ui;

import android.app.Application;

import java.time.Duration;

import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.sscd.android.AndroidKeystoreSscd;
import fi.methics.musap.sdk.sscd.methicsdemo.MethicsDemoSettings;
import fi.methics.musap.sdk.sscd.methicsdemo.MethicsDemoSscd;
import fi.methics.musap.sdk.sscd.yubikey.YubiKeySscd;

public class MusapApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MusapClient.init(this);

        MusapClient.enableSscd(new AndroidKeystoreSscd(this));
        MusapClient.enableSscd(new YubiKeySscd(this));
        MusapClient.enableSscd(new MethicsDemoSscd(this,
                new MethicsDemoSettings("https://demo.methics.fi/appactivation/appactivation/sign?msisdn=",
                        "http://www.methics.fi/KiuruMSSP/v3.2.0#PKCS1",
                        "http://uri.etsi.org/TS102204/v1.1.2#CMS-Signature", Duration.ofMinutes(2)))
        );

    }
}
