package fi.methics.musap.sdk.sscd;

import android.content.Context;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.UUID;

import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keydiscovery.KeyMetaDataStorage;
import fi.methics.musap.sdk.keygeneration.AndroidKeyGenerator;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPPublicKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;
import fi.methics.musap.sdk.util.MLog;

public class AndroidKeystoreSscd implements MUSAPSscdInterface {

    private Context context;

    public AndroidKeystoreSscd(Context context) {
        this.context = context;
    }

    public static final String SSCD_TYPE = "ANDROID_KEYSTORE";
    @Override
    public MUSAPKey bindKey(KeyBindReq req) {
        // "Old" keys cannot be bound to MUSAP.
        // Use generateKey instead.
        throw new UnsupportedOperationException();
    }

    @Override
    public MUSAPKey generateKey(KeyGenReq req) throws Exception {

        // 1. Call Android API to generate a keypair
        // 2. Create a MUSAPKey from the keypair
        // 3. Store the MUSAPKey into fi.methics.musap.keydiscovery.KeyMetaDataStorage
        // 4. Return the MUSAPKey

        MLog.d("Generating a key in Android keystore");
        KeyPair keyPair = new AndroidKeyGenerator().generateKey(req);

        MUSAPKey key = new MUSAPKey.Builder()
                .setKeyName(req.getKeyAlias())
                .setPublicKey(new MUSAPPublicKey(keyPair.getPublic().getEncoded()))
                .setSscdType(this.getSscdInfo().getSscdType())
                .setSscdId(this.getSscdInfo().getSscdId())
                .build();
        new KeyMetaDataStorage(this.context).storeKey(key);
        return key;
    }

    @Override
    public MUSAPSscd getSscdInfo() {
        return new MUSAPSscd.Builder()
                .setSscdName("Android KeyStore")
                .setSscdType(SSCD_TYPE)
                .setCountry("FI")
                .setProvider("Google")
                .setKeygenSupported(true)
                .setSupportedKeyAlgorithms(Arrays.asList("RSA2048"))
                .setSscdId(UUID.randomUUID().toString())
                .build();
    }
}
