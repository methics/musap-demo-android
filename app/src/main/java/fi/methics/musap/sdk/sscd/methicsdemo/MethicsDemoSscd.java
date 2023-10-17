package fi.methics.musap.sdk.sscd.methicsdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

import fi.methics.musap.sdk.internal.datatype.MusapKeyAlgorithm;
import fi.methics.musap.sdk.internal.datatype.MusapSignatureFormat;
import fi.methics.musap.sdk.internal.util.MBase64;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.internal.discovery.KeyBindReq;
import fi.methics.musap.sdk.internal.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.internal.datatype.KeyURI;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.MusapLoA;
import fi.methics.musap.sdk.internal.datatype.MusapSscd;
import fi.methics.musap.sdk.internal.datatype.CmsSignature;
import fi.methics.musap.sdk.internal.datatype.MusapSignature;
import fi.methics.musap.sdk.internal.sign.SignatureReq;
import fi.methics.musap.sdk.internal.util.MLog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;

public class MethicsDemoSscd implements MusapSscdInterface<MethicsDemoSettings> {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String SSCD_TYPE         = "demo";
    public static final String ATTRIBUTE_MSISDN  = "msisdn";

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();


    private Context             context;
    private MethicsDemoSettings settings;
    private OkHttpClient        client;

    public MethicsDemoSscd(Context context, MethicsDemoSettings settings) {
        this.context  = context;
        this.settings = settings;
        this.client   = new OkHttpClient.Builder().readTimeout(settings.getTimeout()).build();
    }

    @Override
    public MusapKey bindKey(KeyBindReq req) {
        throw new java.lang.UnsupportedOperationException(); // TODO: This should actually bind
        // TODO:
        // 1. Call https://demo.methics.fi/appactivation/appactivation/sign?msisdn=35847004112
        // 2. Parse response into a MUSAPKey
        // 3. Store the MUSAPKey into fi.methics.musap.keydiscovery.KeyMetaDataStorage
        // 4. Return the MUSAPKey
        //return null;
    }

    @Override
    public MusapKey generateKey(KeyGenReq req) throws Exception {

        CompletableFuture<KeyGenerationResult> future = new CompletableFuture<>();
        openKeygenPopup(req, future);

        KeyGenerationResult result = future.get();
        if (result.key       != null) return result.key;
        if (result.exception != null) throw result.exception;

        throw new MusapException("Keygen failed");
    }

    @Override
    public MusapSignature sign(SignatureReq req) throws Exception {
        Gson gson = new Gson();
        DemoSigReq jReq = new DemoSigReq();
        jReq.msisdn  = req.getKey().getAttributeValue(ATTRIBUTE_MSISDN);
        jReq.message = "Sign with MUSAP"; // TODO
        jReq.dtbs    = MBase64.toBase64String(req.getData());

        RequestBody body = RequestBody.create(gson.toJson(jReq), JSON);
        Request request = new Request.Builder()
                .url(this.settings.getDemoUrl() + jReq.msisdn)
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
                throw new MusapException("Failed to sign: " + jResp.statuscode);
            }

            return new CmsSignature(MBase64.toBytes(jResp.signature), req.getKey(), req.getAlgorithm());
        }
    }

    @Override
    public MusapSscd getSscdInfo() {
        return new MusapSscd.Builder()
                .setSscdName("Methics Demo")
                .setSscdType(SSCD_TYPE)
                .setCountry("FI")
                .setProvider("Methics")
                .setKeygenSupported(true /* TODO: This should be false */)
                .setSupportedAlgorithms(Arrays.asList(MusapKeyAlgorithm.RSA_2K))
                .setSupportedFormats(Arrays.asList(MusapSignatureFormat.RAW, MusapSignatureFormat.CMS))
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
                    MusapKey key = _generateKey(req, msisdnEditText.getText().toString());
                    future.complete(new KeyGenerationResult(key));
                } catch (MusapException e) {
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

    private MusapKey _generateKey(KeyGenReq req, String msisdn) throws MusapException {

        MLog.d("Sending keygen request to Methics demo for MSISDN " + msisdn);

        DemoSigReq jReq = new DemoSigReq();
        jReq.msisdn  = msisdn;
        jReq.message = "Activate MUSAP";

        try {
            Gson gson = new Gson();
            RequestBody body = RequestBody.create(gson.toJson(jReq), JSON);
            Request request = new Request.Builder()
                    .url(this.getSettings().getDemoUrl() + jReq.msisdn)
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

                CmsSignature signature = new CmsSignature(MBase64.toBytes(jResp.signature));
                MusapKey.Builder builder = new MusapKey.Builder();
                builder.setCertificate(signature.getSignerCertificate());
                builder.setKeyName(req.getKeyAlias());
                builder.setSscdType("Methics Demo");
                builder.setKeyUri(new KeyURI(req.getKeyAlias(), this.getSscdInfo().getSscdType(), "loa3").getUri());
                builder.setSscdId(this.getSscdInfo().getSscdId());
                builder.setLoa(Arrays.asList(MusapLoA.EIDAS_SUBSTANTIAL, MusapLoA.ISO_LOA3));
                builder.setKeyAttribute(ATTRIBUTE_MSISDN, msisdn);
                return builder.build();
            }
        } catch (Exception e) {
            throw new MusapException(e);
        }
    }

    private static class KeyGenerationResult {

        public MusapKey key;
        public MusapException exception;

        public KeyGenerationResult(MusapKey key) {
            this.key = key;
        }

        public KeyGenerationResult(MusapException e) {
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
