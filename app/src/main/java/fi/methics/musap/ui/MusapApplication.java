package fi.methics.musap.ui;

import android.app.Application;

import java.time.Duration;

import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.sscd.android.AndroidKeystoreSscd;
import fi.methics.musap.sdk.sscd.methicsdemo.MethicsDemoSettings;
import fi.methics.musap.sdk.sscd.methicsdemo.MethicsDemoSscd;
import fi.methics.musap.sdk.sscd.rest204.Rest204Settings;
import fi.methics.musap.sdk.sscd.rest204.Rest204Sscd;
import fi.methics.musap.sdk.sscd.yubikey.YubiKeyOpenPgpSscd;
import fi.methics.musap.sdk.sscd.yubikey.YubiKeySscd;

public class MusapApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        MusapClient.init(this);

        MusapClient.enableSscd(new AndroidKeystoreSscd(this));
        MusapClient.enableSscd(new YubiKeySscd(this));
        MusapClient.enableSscd(new YubiKeyOpenPgpSscd(this));
        MusapClient.enableSscd(new MethicsDemoSscd(this,
                new MethicsDemoSettings("https://demo.methics.fi/appactivation/appactivation/sign?msisdn=",
                        "http://www.methics.fi/KiuruMSSP/v3.2.0#PKCS1",
                        "http://uri.etsi.org/TS102204/v1.1.2#CMS-Signature", Duration.ofMinutes(2)))
        );

        Rest204Settings rest204Settings = new Rest204Settings("https://demo.methics.fi/rest/service",
                "http://www.methics.fi/KiuruMSSP/v3.2.0#PKCS1",
                "http://uri.etsi.org/TS102204/v1.1.2#CMS-Signature",
                Duration.ofMinutes(2));
        rest204Settings.setApId("http://musap-ap");
        rest204Settings.setApiKey("LGTiKluF7uvV9uwdK2Zk8v3yRm0Thxz8CDk3gLVcNNV5uH4s");

        MusapClient.enableSscd(new Rest204Sscd(this, rest204Settings));
    }
}
