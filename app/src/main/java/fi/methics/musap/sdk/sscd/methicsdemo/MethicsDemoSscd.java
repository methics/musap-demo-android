package fi.methics.musap.sdk.sscd.methicsdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.EditText;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public static final String SSCD_TYPE         = "demo";
    public static final String SETTINGS_DEMO_URL = "demourl";
    public static final String ATTRIBUTE_MSISDN  = "msisdn";

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();


    private Context             context;
    private OkHttpClient        client   = new OkHttpClient.Builder().readTimeout(Duration.ofMinutes(2)).build();
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

        CompletableFuture<KeyGenerationResult> future = new CompletableFuture<>();
        openKeygenPopup(req, future);

        KeyGenerationResult result = future.get();
        if (result.key       != null) return result.key;
        if (result.exception != null) throw result.exception;

        throw new MUSAPException("Keygen failed");
    }

    @Override
    public MUSAPSignature sign(SignatureReq req) throws Exception {
        Gson gson = new Gson();
        DemoSigReq jReq = new DemoSigReq();
        jReq.msisdn  = req.getKey().getAttributeValue(ATTRIBUTE_MSISDN);
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

    private void openKeygenPopup(KeyGenReq req, CompletableFuture<KeyGenerationResult> future) {

        PopupWindow popupWindow = new PopupWindow(context);
        TextView   popupContent = new TextView(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        popupContent.setText("Please enter MSISDN");
        popupContent.setBackgroundColor(Color.LTGRAY);
        popupContent.setPadding(20, 20, 20, 20);

        EditText msisdnEditText = new EditText(context);
        msisdnEditText.setHint("Enter MSISDN");
        msisdnEditText.setInputType(InputType.TYPE_CLASS_PHONE);

        Button button = new Button(context);
        button.setText("Generate Key");
        button.setOnClickListener(v -> {
            CompletableFuture.runAsync(() -> {
                try {
                    MUSAPKey key = _generateKey(req, msisdnEditText.getText().toString());
                    future.complete(new KeyGenerationResult(key));
                } catch (MUSAPException e) {
                    MLog.e("Failed to generate key", e);
                    future.complete(new KeyGenerationResult(e));
                }
            });
            popupWindow.dismiss();
        });

        layout.addView(popupContent);
        layout.addView(msisdnEditText);
        layout.addView(button);

        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(Color.LTGRAY);
        backgroundDrawable.setCornerRadius(16);

        popupWindow.setBackgroundDrawable(backgroundDrawable);

        popupWindow.setContentView(layout);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        req.getActivity().runOnUiThread(() -> popupWindow.showAtLocation(req.getView(), Gravity.CENTER, 0, 0));
    }

    private MUSAPKey _generateKey(KeyGenReq req, String msisdn) throws MUSAPException {

        MLog.d("Sending keygen request to Methics demo for MSISDN " + msisdn);

        DemoSigReq jReq = new DemoSigReq();
        jReq.msisdn  = msisdn;
        jReq.message = "Activate MUSAP";

        try {
            Gson gson = new Gson();
            RequestBody body = RequestBody.create(gson.toJson(jReq), JSON);
            Request request = new Request.Builder()
                    .url(this.getSettings().getSetting(SETTINGS_DEMO_URL) + jReq.msisdn)
                    .post(body)
                    .build();
            MLog.d("Sending request " + gson.toJson(jReq));
            try (Response response = client.newCall(request).execute()) {

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
                builder.setKeyAttribute(ATTRIBUTE_MSISDN, msisdn);
                return builder.build();
            }
        } catch (Exception e) {
            throw new MUSAPException(e);
        }
    }

    private static class KeyGenerationResult {

        public MUSAPKey key;
        public MUSAPException exception;

        public KeyGenerationResult(MUSAPKey key) {
            this.key = key;
        }

        public KeyGenerationResult(MUSAPException e) {
            this.exception = e;
        }

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
