package fi.methics.musap.sdk.sscd.android;

import android.content.Context;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Signature;
import java.util.Arrays;

import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.discovery.KeyBindReq;
import fi.methics.musap.sdk.discovery.MetadataStorage;
import fi.methics.musap.sdk.keygeneration.AndroidKeyGenerator;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MusapKey;
import fi.methics.musap.sdk.keyuri.MusapSscd;
import fi.methics.musap.sdk.sign.MusapSignature;
import fi.methics.musap.sdk.sign.SignatureReq;
import fi.methics.musap.sdk.util.MLog;

public class AndroidKeystoreSscd implements MusapSscdInterface<AndroidKeystoreSettings> {

    private Context context;

    private AndroidKeystoreSettings settings = new AndroidKeystoreSettings();

    public AndroidKeystoreSscd(Context context) {
        this.context = context;
    }

    public static final String SSCD_TYPE = "aks";
    @Override
    public MusapKey bindKey(KeyBindReq req) {
        // "Old" keys cannot be bound to MUSAP.
        // Use generateKey instead.
        throw new UnsupportedOperationException();
    }

    @Override
    public MusapKey generateKey(KeyGenReq req) throws Exception {

        // 1. Call Android API to generate a keypair
        // 2. Create a MUSAPKey from the keypair
        // 3. Store the MUSAPKey into fi.methics.musap.keydiscovery.KeyMetaDataStorage
        // 4. Return the MUSAPKey
        MLog.d("Generating a key in Android keystore");
        MusapKey key = new AndroidKeyGenerator().generateKey(req, this.getSscdInfo());
        MetadataStorage storage = new MetadataStorage(this.context);
        storage.storeKey(key, this.getSscdInfo());
        return key;
    }

    @Override
    public MusapSignature sign(SignatureReq req) throws GeneralSecurityException, IOException {
        String alias = req.getKey().getKeyName();

        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        KeyStore.Entry entry = ks.getEntry(alias, null);
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            MLog.d("Not an instance of a PrivateKeyEntry");
            return null;
        }
        Signature s = Signature.getInstance("SHA256withECDSA");
        s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
        s.update(req.getData());
        byte[] signature = s.sign();

        return new MusapSignature(signature);
    }

    @Override
    public MusapSscd getSscdInfo() {
        return new MusapSscd.Builder()
                .setSscdName("Android KeyStore")
                .setSscdType(SSCD_TYPE)
                .setCountry("FI")
                .setProvider("Google")
                .setKeygenSupported(true)
                .setSupportedKeyAlgorithms(Arrays.asList("RSA2048"))
                .setSscdId("AKS") // TODO: This needs to be SSCD instance specific
                .build();
    }

    @Override
    public AndroidKeystoreSettings getSettings() {
        return settings;
    }

}
