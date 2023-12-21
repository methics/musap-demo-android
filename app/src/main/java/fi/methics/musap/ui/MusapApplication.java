package fi.methics.musap.ui;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import fi.methics.musap.sdk.api.MusapCallback;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.internal.datatype.MusapLink;
import fi.methics.musap.sdk.internal.datatype.RelyingParty;
import fi.methics.musap.sdk.internal.util.MLog;
import fi.methics.musap.sdk.sscd.android.AndroidKeystoreSscd;
import fi.methics.musap.sdk.sscd.external.ExternalSscd;
import fi.methics.musap.sdk.sscd.external.ExternalSscdSettings;
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


        MethicsDemoSettings demoSettings = new MethicsDemoSettings("https://demo.methics.fi/appactivation/appactivation/sign?msisdn=");
        demoSettings.setSscdName("Alauda PBY");
        MusapClient.enableSscd(new MethicsDemoSscd(this, demoSettings));
        MusapClient.enableSscd(new YubiKeyOpenPgpSscd(this));
        Rest204Settings rest204Settings = new Rest204Settings("https://demo.methics.fi/rest/service");
        rest204Settings.setApId("http://musap-ap");
        rest204Settings.setApiKey("LGTiKluF7uvV9uwdK2Zk8v3yRm0Thxz8CDk3gLVcNNV5uH4s");
        rest204Settings.enableNoSpam();
        rest204Settings.setDtbdEnabled(false);
        rest204Settings.setSscdName("Mobiilivarmenne");
        rest204Settings.setRawFormat("http://mss.ficom.fi/TS102204/v1.0.0#PKCS1");
        rest204Settings.setBindSignatureProfile("http://mss.ficom.fi/TS102206/v1.0.0/signature-profile.xml");
        rest204Settings.setSignatureProfile("http://mss.ficom.fi/TS102206/v1.0.0/digestive-signature-profile.xml");
        MusapClient.enableSscd(new Rest204Sscd(this, rest204Settings));

        ExternalSscdSettings settings = new ExternalSscdSettings("LOCAL");
        settings.setSscdName("server-eemeli");
        MusapClient.enableSscd(new ExternalSscd(this, settings));

        if (!MusapClient.isLinkEnabled()) {
            MusapClient.enableLink("https://demo.methics.fi/musapdemo", null, new MusapCallback<MusapLink>() {
                @Override
                public void onSuccess(MusapLink o) {
                    MLog.d("Enrolled successfully");
                    MusapClient.coupleWithRelyingParty("FN6DDY", new MusapCallback<RelyingParty>() {
                        @Override
                        public void onSuccess(RelyingParty relyingParty) {
                            MLog.d("Linked successfully");
                        }
                        @Override
                        public void onException(MusapException e) {
                            MLog.e("Linking failed", e);
                        }
                    });
                }
                @Override
                public void onException(MusapException e) {
                    MLog.e("Failed to enroll", e);
                }
            });
        }

    }

}
