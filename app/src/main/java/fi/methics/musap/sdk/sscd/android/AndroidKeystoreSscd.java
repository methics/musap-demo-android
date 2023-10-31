package fi.methics.musap.sdk.sscd.android;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Arrays;

import fi.methics.musap.sdk.api.MusapConstants;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.internal.datatype.KeyURI;
import fi.methics.musap.sdk.internal.datatype.KeyAlgorithm;
import fi.methics.musap.sdk.internal.datatype.MusapLoA;
import fi.methics.musap.sdk.internal.datatype.PublicKey;
import fi.methics.musap.sdk.internal.datatype.SignatureAlgorithm;
import fi.methics.musap.sdk.internal.datatype.SignatureFormat;
import fi.methics.musap.sdk.internal.discovery.KeyBindReq;
import fi.methics.musap.sdk.internal.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.MusapSscd;
import fi.methics.musap.sdk.internal.datatype.MusapSignature;
import fi.methics.musap.sdk.internal.sign.SignatureReq;
import fi.methics.musap.sdk.internal.util.MBase64;
import fi.methics.musap.sdk.internal.util.MLog;

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

        MusapSscd                  sscd = this.getSscdInfo();
        String                algorithm = this.resolveAlgorithm(req);
        AlgorithmParameterSpec algSspec = this.resolveAlgorithmParameterSpec(req);

        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(req.getKeyAlias(),
                KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512);

        if (algSspec != null) builder.setAlgorithmParameterSpec(algSspec);
        KeyGenParameterSpec spec = builder.build();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm, "AndroidKeyStore");
        kpg.initialize(spec);

        KeyPair keyPair = kpg.generateKeyPair();
        MLog.d("Key generation successful");

        MusapKey generatedKey = new MusapKey.Builder()
                .setSscdType(MusapConstants.ANDROID_KS_TYPE)
                .setKeyName(req.getKeyAlias())
                .setKeyUri(new KeyURI(req.getKeyAlias(), sscd.getSscdType(), "loa3").getUri())
                .setSscdId(sscd.getSscdId())
                .setLoa(Arrays.asList(MusapLoA.EIDAS_SUBSTANTIAL, MusapLoA.ISO_LOA3))
                .setPublicKey(new PublicKey(keyPair))
                .build();
        MLog.d("Generated key with KeyURI " + generatedKey.getKeyUri());

        return generatedKey;
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

        SignatureAlgorithm algorithm = req.getAlgorithm();
        MLog.d("Signing " + MBase64.toBase64String(req.getData()) + " with algorithm " + algorithm);
        Signature s = Signature.getInstance(algorithm.getJavaAlgorithm());
        s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
        s.update(req.getData());
        byte[] signature = s.sign();

        return new MusapSignature(signature, req.getKey(), algorithm, req.getFormat());
    }

    @Override
    public MusapSscd getSscdInfo() {
        return new MusapSscd.Builder()
                .setSscdName("Android KeyStore")
                .setSscdType(SSCD_TYPE)
                .setCountry("FI")
                .setProvider("Google")
                .setKeygenSupported(true)
                .setSupportedAlgorithms(Arrays.asList(
                        KeyAlgorithm.RSA_2K,
                        KeyAlgorithm.ECC_P256_R1,
                        KeyAlgorithm.ECC_P256_K1,
                        KeyAlgorithm.ECC_P384_K1))
                .setSupportedFormats(Arrays.asList(SignatureFormat.RAW))
                .setSscdId("AKS") // TODO: This needs to be SSCD instance specific
                .build();
    }

    @Override
    public AndroidKeystoreSettings getSettings() {
        return settings;
    }

    /**
     * Resolve the {@link AlgorithmParameterSpec} to use with key generation
     * @param req Key generation request
     * @return AlgorithmParameterSpec
     */
    private AlgorithmParameterSpec resolveAlgorithmParameterSpec(KeyGenReq req) {
        KeyAlgorithm algorithm = req.getAlgorithm();
        if (algorithm == null) return null;
        if (algorithm.isRsa()) {
            return new RSAKeyGenParameterSpec(algorithm.bits, RSAKeyGenParameterSpec.F4);
        } else {
            return new ECGenParameterSpec(algorithm.curve);
        }
    }

    /**
     * Resolve the key algorithm (EC or RSA)
     * @param req Key generation request
     * @return "EC" or "RSA" (default EC)
     */
    private String resolveAlgorithm(KeyGenReq req) {

        KeyAlgorithm algorithm = req.getAlgorithm();
        if (algorithm == null) return KeyProperties.KEY_ALGORITHM_EC;
        if (algorithm.isRsa()) {
            return KeyProperties.KEY_ALGORITHM_RSA;
        } else {
            return KeyProperties.KEY_ALGORITHM_EC;
        }
    }

}
