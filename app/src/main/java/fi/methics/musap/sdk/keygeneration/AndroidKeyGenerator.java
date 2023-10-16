package fi.methics.musap.sdk.keygeneration;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Arrays;

import fi.methics.musap.sdk.api.MusapConstants;
import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MusapKey;
import fi.methics.musap.sdk.keyuri.MusapLoA;
import fi.methics.musap.sdk.keyuri.MusapPublicKey;
import fi.methics.musap.sdk.keyuri.MusapSscd;
import fi.methics.musap.sdk.util.MLog;

public class AndroidKeyGenerator {

    public MusapKey generateKey(KeyGenReq req, MusapSscd sscd) throws Exception {
        String algorithm = this.resolveAlgorithm(req);

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(
                algorithm, "AndroidKeyStore");
        kpg.initialize(new KeyGenParameterSpec.Builder(
                req.getKeyAlias(),
                KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                .setDigests(KeyProperties.DIGEST_SHA256,
                            KeyProperties.DIGEST_SHA512)
                .build());

        KeyPair keyPair = kpg.generateKeyPair();

        MLog.d("Key generation successful");

        MusapKey generatedKey = new MusapKey.Builder()
                .setSscdType(MusapConstants.ANDROID_KS_TYPE)
                .setKeyName(req.getKeyAlias())
                .setKeyUri(new KeyURI(req.getKeyAlias(), sscd.getSscdType(), "loa3").getUri())
                .setSscdId(sscd.getSscdId())
                .setLoa(Arrays.asList(MusapLoA.EIDAS_SUBSTANTIAL, MusapLoA.ISO_LOA3))
                .setPublicKey(new MusapPublicKey(keyPair))
                .build();
        MLog.d("Generated key with KeyURI " + generatedKey.getKeyUri());

        return generatedKey;
    }

    private String resolveAlgorithm(KeyGenReq req) {
        if (req.getType() == null) {
            return KeyProperties.KEY_ALGORITHM_EC;
        }

        switch (req.getType()) {
            case Ed25519:
                return KeyProperties.KEY_ALGORITHM_EC;

            default:
                return KeyProperties.KEY_ALGORITHM_EC;
        }

    }
}
