package fi.methics.musap.sdk.keygeneration;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import fi.methics.musap.sdk.util.MLog;

public class AndroidKeyGenerator {

    public KeyPair generateKey(KeyGenReq req) throws Exception {
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
        return keyPair;
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
