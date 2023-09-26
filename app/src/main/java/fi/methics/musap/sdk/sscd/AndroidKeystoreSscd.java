package fi.methics.musap.sdk.sscd;

import android.content.Context;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Signature;
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
import fi.methics.musap.sdk.sign.MUSAPSignature;
import fi.methics.musap.sdk.sign.SignatureReq;
import fi.methics.musap.sdk.util.MLog;

public class AndroidKeystoreSscd implements MUSAPSscdInterface {

    private Context context;

    public AndroidKeystoreSscd(Context context) {
        this.context = context;
    }

    public static final String SSCD_TYPE = "aks";
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
        MUSAPKey key = new AndroidKeyGenerator().generateKey(req, this.getSscdInfo());
        new KeyMetaDataStorage(this.context).storeKey(key);
        return key;
    }

    @Override
    public MUSAPSignature sign(SignatureReq req) throws GeneralSecurityException, IOException {
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

        return new MUSAPSignature(signature);
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
                .setSscdId("AKS") // TODO: This needs to be SSCD instance specific
                .build();
    }
}
