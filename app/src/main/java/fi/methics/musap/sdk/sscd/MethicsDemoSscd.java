package fi.methics.musap.sdk.sscd;

import android.content.Context;

import java.util.Arrays;

import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;

public class MethicsDemoSscd implements MUSAPSscdInterface {
    private Context context;

    public MethicsDemoSscd(Context context) {
        this.context = context;
    }
    public static final String SSCD_TYPE = "METHICS_DEMO";
    @Override
    public MUSAPKey bindKey(KeyBindReq req) {
        // TODO:
        // 1. Call https://demo.methics.fi/appactivation/appactivation/sign?msisdn=35847004112
        // 2. Parse response into a MUSAPKey
        // 3. Store the MUSAPKey into fi.methics.musap.keydiscovery.KeyMetaDataStorage
        // 4. Return the MUSAPKey
        return null;
    }

    @Override
    public MUSAPKey generateKey(KeyGenReq req) throws Exception {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public MUSAPSscd getSscdInfo() {
        return new MUSAPSscd.Builder()
                .setSscdName("Methics Demo")
                .setSscdType(SSCD_TYPE)
                .setCountry("FI")
                .setProvider("Methics")
                .setKeygenSupported(false)
                .setSupportedKeyAlgorithms(Arrays.asList("RSA2048"))
                .build();
    }
}
