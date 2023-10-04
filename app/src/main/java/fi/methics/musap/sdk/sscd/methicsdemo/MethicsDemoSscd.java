package fi.methics.musap.sdk.sscd.methicsdemo;

import android.content.Context;

import java.util.Arrays;

import fi.methics.musap.sdk.util.MBase64;
import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.discovery.KeyBindReq;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPLoa;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;
import fi.methics.musap.sdk.sign.CMSSignature;
import fi.methics.musap.sdk.sign.MUSAPSignature;
import fi.methics.musap.sdk.sign.SignatureReq;
import fi.methics.musap.sdk.util.MLog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;

public class MethicsDemoSscd implements MUSAPSscdInterface<MethicsDemoSettings> {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String SSCD_TYPE = "demo";

    public static final String SETTINGS_DEMO_URL = "demourl";


    private Context context;
    private OkHttpClient client = new OkHttpClient();


    private MethicsDemoSettings settings = new MethicsDemoSettings();

    {
        settings.getSettings().put(SETTINGS_DEMO_URL, "https://demo.methics.fi/appactivation/appactivation/sign?msisdn=");
    }

    public MethicsDemoSscd(Context context) {
        this.context = context;
    }

    @Override
    public MUSAPKey bindKey(KeyBindReq req) {
        throw new java.lang.UnsupportedOperationException(); // TODO: This should actually bind
        // TODO:
        // 1. Call https://demo.methics.fi/appactivation/appactivation/sign?msisdn=35847004112
        // 2. Parse response into a MUSAPKey
        // 3. Store the MUSAPKey into fi.methics.musap.keydiscovery.KeyMetaDataStorage
        // 4. Return the MUSAPKey
        //return null;
    }

    @Override
    public MUSAPKey generateKey(KeyGenReq req) throws Exception {

        Gson gson = new Gson();
        DemoSigReq jReq = new DemoSigReq();
        jReq.msisdn  = "35847001001";
        jReq.message = "Activate MUSAP";

        RequestBody body = RequestBody.create(gson.toJson(jReq), JSON);
        Request request = new Request.Builder()
                .url(this.getSettings().getSetting(SETTINGS_DEMO_URL) + jReq.msisdn)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {

            MLog.d("Sending request " + gson.toJson(jReq));
            String sResp = response.body().string();
            MLog.d("Got response " + sResp);

            DemoSigResp jResp = gson.fromJson(sResp, DemoSigResp.class);

            if ("500".equals(jResp.statuscode)) {
                MLog.d("Successfully bound Methics Demo SSCD");
            }

            CMSSignature signature = new CMSSignature(MBase64.toBytes(jResp.signature));
            MUSAPKey.Builder builder = new MUSAPKey.Builder();
            builder.setCertificate(signature.getSignerCertificate());
            builder.setKeyName(req.getKeyAlias());
            builder.setSscdType("Methics Demo");
            builder.setKeyUri(new KeyURI(req.getKeyAlias(), this.getSscdInfo().getSscdType(), "loa3").getUri());
            builder.setSscdId(this.getSscdInfo().getSscdId());
            builder.setLoa(Arrays.asList(MUSAPLoa.EIDAS_SUBSTANTIAL, MUSAPLoa.ISO_LOA3));
            return builder.build();
        }
        //throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public MUSAPSignature sign(SignatureReq req) throws Exception {
        Gson gson = new Gson();
        DemoSigReq jReq = new DemoSigReq();
        jReq.msisdn  = "35847001001";
        jReq.message = "Sign with MUSAP"; // TODO
        jReq.dtbs    = MBase64.toBase64String(req.getData());

        RequestBody body = RequestBody.create(gson.toJson(jReq), JSON);
        Request request = new Request.Builder()
                .url(this.getSettings().getSetting(SETTINGS_DEMO_URL) + jReq.msisdn)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {

            MLog.d("Sending request " + gson.toJson(jReq));
            String sResp = response.body().string();
            MLog.d("Got response " + sResp);

            DemoSigResp jResp = gson.fromJson(sResp, DemoSigResp.class);

            if ("500".equals(jResp.statuscode)) {
                MLog.d("Successfully signed");
            } else {
                throw new MUSAPException("Failed to sign: " + jResp.statuscode);
            }

            return new CMSSignature(MBase64.toBytes(jResp.signature));
        }
    }

    @Override
    public MUSAPSscd getSscdInfo() {
        return new MUSAPSscd.Builder()
                .setSscdName("Methics Demo")
                .setSscdType(SSCD_TYPE)
                .setCountry("FI")
                .setProvider("Methics")
                .setKeygenSupported(true /* TODO: This should be false */)
                .setSupportedKeyAlgorithms(Arrays.asList("RSA2048"))
                .setSscdId("METHICS_DEMO") // TODO: This needs to be SSCD instance specific
                .build();
    }

    @Override
    public MethicsDemoSettings getSettings() {
        return settings;
    }

    private static class DemoSigReq {

        public String type = "sign";
        public String msisdn;
        public String message;
        public String dtbs;
        public String mimetype;
        public String encoding;

    }

    private static class DemoSigResp {

        public String type;
        public String statuscode;
        public String signature;

    }

}
