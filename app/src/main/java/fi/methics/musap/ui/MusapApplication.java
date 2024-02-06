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

        MusapClient.enableSscd(new AndroidKeystoreSscd(this), "1");
        MusapClient.enableSscd(new YubiKeySscd(this), "2");


        MethicsDemoSettings demoSettings = new MethicsDemoSettings("https://demo.methics.fi/appactivation/appactivation/sign?msisdn=");
        demoSettings.setSscdName("Alauda PBY");
        MusapClient.enableSscd(new MethicsDemoSscd(this,demoSettings), "3");
        MusapClient.enableSscd(new YubiKeyOpenPgpSscd(this), "4");
        Rest204Settings rest204Settings = new Rest204Settings("https://demo.methics.fi/rest/service");
        rest204Settings.setApId("http://musap-ap");
        rest204Settings.setApiKey("LGTiKluF7uvV9uwdK2Zk8v3yRm0Thxz8CDk3gLVcNNV5uH4s");
        rest204Settings.enableNoSpam();
        rest204Settings.setDtbdEnabled(false);
        rest204Settings.setSscdName("Mobiilivarmenne");
        rest204Settings.setRawFormat("http://mss.ficom.fi/TS102204/v1.0.0#PKCS1");
        rest204Settings.setBindSignatureProfile("http://mss.ficom.fi/TS102206/v1.0.0/signature-profile.xml");
        rest204Settings.setSignatureProfile("http://mss.ficom.fi/TS102206/v1.0.0/digestive-signature-profile.xml");
        MusapClient.enableSscd(new Rest204Sscd(this, rest204Settings), "5");
    }

}
