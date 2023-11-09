package fi.methics.musap.sdk.sscd.yubikey;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yubico.yubikit.android.YubiKitManager;
import com.yubico.yubikit.android.transport.nfc.NfcConfiguration;
import com.yubico.yubikit.android.transport.nfc.NfcNotAvailable;
import com.yubico.yubikit.android.transport.nfc.NfcYubiKeyDevice;
import com.yubico.yubikit.core.smartcard.SmartCardConnection;
import com.yubico.yubikit.piv.KeyType;
import com.yubico.yubikit.piv.ManagementKeyType;
import com.yubico.yubikit.piv.PivSession;
import com.yubico.yubikit.piv.Slot;
import com.yubico.yubikit.piv.jca.PivAlgorithmParameterSpec;
import com.yubico.yubikit.piv.jca.PivProvider;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.internal.datatype.KeyAlgorithm;
import fi.methics.musap.sdk.internal.datatype.SignatureFormat;
import fi.methics.musap.sdk.internal.discovery.KeyBindReq;
import fi.methics.musap.sdk.internal.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.internal.datatype.KeyURI;
import fi.methics.musap.sdk.internal.datatype.MusapCertificate;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.MusapLoA;
import fi.methics.musap.sdk.internal.datatype.MusapSscd;
import fi.methics.musap.sdk.internal.datatype.MusapSignature;
import fi.methics.musap.sdk.internal.sign.SignatureReq;
import fi.methics.musap.sdk.internal.util.KeyGenerationResult;
import fi.methics.musap.sdk.internal.util.MLog;
import fi.methics.musap.sdk.internal.util.SigningResult;

public class YubiKeySscd implements MusapSscdInterface<YubiKeySettings> {

    private static final byte[] MANAGEMENT_KEY = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final ManagementKeyType TYPE = ManagementKeyType.TDES;

    private static final String SSCD_TYPE        = "Yubikey";
    private static final String ATTRIBUTE_SERIAL = "SerialNumber";

    private YubiKeySettings settings = new YubiKeySettings();

    private AlertDialog currentPrompt;
    private final ManagementKeyType type;

    private final byte[] managementKey;

    private final YubiKitManager yubiKitManager;

    private final Context c;

    public YubiKeySscd(Context context) {
        this.managementKey = MANAGEMENT_KEY;
        this.type = TYPE;
        this.c = context;
        this.yubiKitManager = new YubiKitManager(this.c);
    }

    @Override
    public MusapKey bindKey(KeyBindReq req) throws Exception {
        // Bind an existing YubiKey keypair to MUSAP by signing with it
        // Get the public key, and verify that it matches 
        return null;
    }

    @Override
    public MusapKey generateKey(KeyGenReq req) throws Exception {
        CompletableFuture<KeyGenerationResult> keygenFuture = new CompletableFuture<>();

        Context c = req.getActivity();
        View v = LayoutInflater.from(c).inflate(R.layout.dialog_pin, null);
        showInsertPinDialog(new SscdTransaction(req, keygenFuture));

        KeyGenerationResult result = keygenFuture.get();
        if (result.key       != null) return result.key;
        if (result.exception != null) throw  result.exception;

        throw new MusapException("Keygen failed");
    }

    @Override
    public MusapSignature sign(SignatureReq req) throws Exception {

        CompletableFuture<SigningResult> signFuture = new CompletableFuture<>();
        this.showInsertPinDialog(new SscdTransaction(req, signFuture));

        SigningResult result = signFuture.get();
        if (result.signature != null) return result.signature;
        if (result.exception != null) throw  result.exception;

        throw new MusapException("Signing failed");
    }

    @Override
    public String generateSscdId(MusapKey key) {
        return SSCD_TYPE + "/" + key.getAttributeValue(ATTRIBUTE_SERIAL);
    }

    private Activity getActivity(SignatureReq sigReq, KeyGenReq keygenReq) {
        if (sigReq    != null) return sigReq.getActivity();
        if (keygenReq != null) return keygenReq.getActivity();
        return null;
    }

    private void showInsertYubiKeyDialog(SscdTransaction txn, String pin, Activity activity) {

        // Dismiss old dialog if it it showing
        activity.runOnUiThread(() -> {
            if (currentPrompt != null) {
                currentPrompt.dismiss();
                currentPrompt.cancel();
            }
        });

        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_insert_yubikey, null);

        activity.runOnUiThread(() -> {
            currentPrompt = new AlertDialog.Builder(activity)
                    .setTitle("Insert YubiKey")
                    .setView(v)
                    .create();
            currentPrompt.show();
        });

        if (txn.keyGenReq != null) {
            this.yubiKeyGen(pin, txn);
        } else {
            this.yubiSign(pin, txn);
        }
    }

    private void showInsertPinDialog(SscdTransaction txn) {
        MLog.d("Showing dialog");

        if (txn == null) {
            MLog.d("Missing request");
            throw new IllegalArgumentException();
        }
        MLog.d("Building view");
        Activity activity = txn.getActivity();
        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_pin, null);

        MLog.d("Running on UI thread. Activity=" + activity.getClass());
        activity.runOnUiThread(() -> {
            MLog.d("Showing prompt");
            this.currentPrompt = new AlertDialog.Builder(activity)
                    .setTitle("PIN")
                    .setView(v)
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        String pin = ((TextView) v.findViewById(R.id.dialog_pin_edittext)).getText().toString();
                        MLog.d("PIN=" + pin);

                        showInsertYubiKeyDialog(txn, pin, activity);
                    })
                    .setNeutralButton("Cancel", (dialogInterface, i) ->  {
                        dialogInterface.cancel();
                    })
                    .create();

            currentPrompt.show();
        });

    }

    public void showRemoveYubiKeyDialog(Activity activity) {

        // Dismiss old dialog if it it showing
        activity.runOnUiThread(() -> {
            if (currentPrompt != null) {
                currentPrompt.dismiss();
                currentPrompt.cancel();
            }
        });

        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_remove_yubikey, null);

        activity.runOnUiThread(() -> {
            currentPrompt = new AlertDialog.Builder(activity)
                    .setTitle("Remove YubiKey")
                    .setView(v)
                    .create();
            currentPrompt.show();
        });
    }

    public void showKeyGenFailedDualog(SscdTransaction txn) {

        // Dismiss old dialog if it it showing
        txn.getActivity().runOnUiThread(() -> {
            if (currentPrompt != null) {
                currentPrompt.dismiss();
            }
        });

        Context c = txn.getActivity();
        View v = LayoutInflater.from(c).inflate(R.layout.dialog_keygen_failed, null);

        txn.getActivity().runOnUiThread(() -> {
            currentPrompt = new AlertDialog.Builder(c)
                    .setTitle("Key Generation Failed")
                    .setView(v)
                    .create();
            currentPrompt.show();
        });
    }

    public void showSignFailedDialog(SscdTransaction txn) {

        // Dismiss old dialog if it it showing
        txn.getActivity().runOnUiThread(() -> {
            if (currentPrompt != null) {
                currentPrompt.dismiss();
            }
        });

        Context c = txn.getActivity();
        View v = LayoutInflater.from(c).inflate(R.layout.dialog_sign_failed, null);

        txn.getActivity().runOnUiThread(() -> {
            currentPrompt = new AlertDialog.Builder(c)
                    .setTitle("Signature Failed")
                    .setView(v)
                    .create();
            currentPrompt.show();
        });
    }


    private void yubiKeyGen(String pin, SscdTransaction txn) {
        try {
            yubiKitManager.startNfcDiscovery(new NfcConfiguration(), txn.getActivity(), device -> {
                MLog.d("Found NFC");
                connect(device, txn, pin);
            });
        } catch (NfcNotAvailable e) {
            if (e.isDisabled()) {
                Toast.makeText(c, "NFC is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "NFC is not available", Toast.LENGTH_SHORT).show();
            }
            yubiKitManager.stopNfcDiscovery(txn.getActivity());
        }
    }

    private void yubiSign(String pin, SscdTransaction txn) {
        try {
            yubiKitManager.startNfcDiscovery(new NfcConfiguration(), txn.getActivity(), device -> {
                MLog.d("Found NFC");
                connectForSign(device, txn, pin);
            });
        } catch (NfcNotAvailable e) {
            if (e.isDisabled()) {
                Toast.makeText(c, "NFC is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "NFC is not available", Toast.LENGTH_SHORT).show();
            }
            yubiKitManager.stopNfcDiscovery(txn.getActivity());
        }
    }

    @Override
    public MusapSscd getSscdInfo() {
        return new MusapSscd.Builder()
                .setSscdName("Yubikey")
                .setSscdType(SSCD_TYPE)
                .setCountry("FI")
                .setProvider("Yubico")
                .setKeygenSupported(true)
                .setSupportedAlgorithms(Arrays.asList(KeyAlgorithm.ECC_P256_K1, KeyAlgorithm.ECC_P384_K1))
                .setSupportedFormats(Arrays.asList(SignatureFormat.RAW))
                .build();
    }

    @Override
    public boolean isKeygenSupported() {
        return true;
    }

    @Override
    public YubiKeySettings getSettings() {
        return settings;
    }

    private void connect(NfcYubiKeyDevice device, SscdTransaction txn, String pin)  {

        device.requestConnection(SmartCardConnection.class, result -> {
            // The result is a Result<SmartCardConnection, IOException>, which represents either a successful connection, or an error.
            try {
                boolean success = result.isSuccess();
                MLog.d("Connection successful=" + success);

                // If the connection is not successful, try again
                if (!success) {
                    MLog.d("Failed to connect");
                    this.showKeyGenFailedDualog(txn);
                } else {
                    MLog.d("PIN=" + pin);
                    keyGenOnDevice(txn, pin, result.getValue());
                }
            } catch (Exception e) {
                MLog.e("Failed to connect", e);
                this.showKeyGenFailedDualog(txn);
                yubiKitManager.stopNfcDiscovery(txn.getActivity());
            }
        });
    }

    private void connectForSign(final NfcYubiKeyDevice device, final SscdTransaction txn, String pin)  {

        device.requestConnection(SmartCardConnection.class, result -> {
            // The result is a Result<SmartCardConnection, IOException>, which represents either a successful connection, or an error.
            try {
                boolean success = result.isSuccess();
                MLog.d("Connection successful=" + success);

                // If the connection is not successful, try again
                if (!success) {
                    MLog.d("Failed to connect");
                    this.showSignFailedDialog(txn);
                    yubiKitManager.stopNfcDiscovery(txn.getActivity());
                } else {
                    MLog.d("PIN=" + pin);
                    signOnDevice(pin, txn, result.getValue());
                }
            } catch (Exception e) {
                MLog.e("Failed to connect", e);
                this.showSignFailedDialog(txn);
                yubiKitManager.stopNfcDiscovery(txn.getActivity());
            }
        });
    }

    private void keyGenOnDevice(SscdTransaction txn, String pin, SmartCardConnection connection) throws Exception {
        PivSession pivSession = new PivSession(connection);
        pivSession.authenticate(this.type, this.managementKey);

        PivProvider pivProvider = new PivProvider(pivSession);
        Security.insertProviderAt(pivProvider, 1); // JCA Security providers are indexed from 1

        KeyPairGenerator ecKpg = KeyPairGenerator.getInstance("YKPivEC");
        MLog.d("Initialized KeyPairGenerator");

        // PinPolicy and TouchPolicy should come from the using app
        // Pin and used slot comes from the user
        final Slot usedSlot = Slot.SIGNATURE;

        ecKpg.initialize(
                new PivAlgorithmParameterSpec(
                        usedSlot,
                        this.resolveKeyType(txn),
                        null, // PinPolicy
                        null, // TouchPolicy
                        pin.toCharArray() // PIV PIN
                )
        );
        KeyPair keyPair = ecKpg.generateKeyPair();

        MLog.d("Generated KeyPair");

        X500Name name = new X500Name("CN=MUSAP Test");
        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(
                name,
                new BigInteger("123456789"),
                new Date(),
                new Date(),
                name,
                SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(keyPair.getPublic().getEncoded()))
        );
        MLog.d("Built cert");

        byte[] certBytes = builder.build(new ContentSigner() {

            private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            @Override
            public AlgorithmIdentifier getAlgorithmIdentifier() {
                return new AlgorithmIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA256);
            }

            @Override
            public OutputStream getOutputStream() {
                return this.buffer;
            }

            @Override
            public byte[] getSignature() {
                try {
                    Signature sig = Signature.getInstance("SHA256withECDSA", pivProvider);
                    sig.initSign(keyPair.getPrivate());
                    sig.update(this.buffer.toByteArray());
                    return sig.sign();
                } catch (Exception e) {
                    MLog.e("Failed to init content signer", e);
                    return null;
                }
            }
        }).getEncoded();

        MLog.d("Encoded cert");
        X509Certificate builtCert = (X509Certificate) CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(certBytes));

        pivSession.putCertificate(usedSlot, builtCert);
        MusapCertificate cert = new MusapCertificate(builtCert);

        KeyGenReq req = txn.keyGenReq;

        MusapKey.Builder keyBuilder = new MusapKey.Builder();
        keyBuilder.setCertificate(cert);
        keyBuilder.setKeyAlias(req.getKeyAlias());
        keyBuilder.addAttribute(ATTRIBUTE_SERIAL, Integer.toHexString(pivSession.getSerialNumber()));
        keyBuilder.setSscdType(this.getSscdInfo().getSscdType());
        keyBuilder.setKeyUri(new KeyURI(req.getKeyAlias(), this.getSscdInfo().getSscdType(), "loa3").getUri());
        keyBuilder.setSscdId(this.getSscdInfo().getSscdId());
        keyBuilder.setLoa(Arrays.asList(MusapLoA.EIDAS_SUBSTANTIAL, MusapLoA.ISO_LOA3));

        txn.keyGenFuture.complete(new KeyGenerationResult(keyBuilder.build()));

        MLog.d("Put certificate to slot");

        showRemoveYubiKeyDialog(txn.getActivity());
    }

    private void signOnDevice(String pin, SscdTransaction txn,  SmartCardConnection connection) throws Exception {

        String msg = "Test string";

        try {
            PivSession pivSession = new PivSession(connection);
            pivSession.authenticate(this.type, this.managementKey);

            Slot slot = Slot.SIGNATURE;

            PivProvider pivProvider = new PivProvider(pivSession);
            Security.insertProviderAt(pivProvider, 1); // JCA Security providers are indexed from 1

            KeyStore keyStore = KeyStore.getInstance("YKPiv", pivProvider);

            keyStore.load(null);

            PublicKey publicKey = keyStore.getCertificate(slot.getStringAlias()).getPublicKey();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(slot.getStringAlias(), pin.toCharArray());

            String algorithm = "SHA256withECDSA";

            Signature signature = Signature.getInstance(algorithm, pivProvider);
            signature.initSign(privateKey);
            signature.update(msg.getBytes(StandardCharsets.UTF_8));
            byte[] sigResult = signature.sign();

            MLog.d("Signed");

            Signature verify = Signature.getInstance(algorithm);
            verify.initVerify(publicKey);
            verify.update(msg.getBytes(StandardCharsets.UTF_8));
            boolean valid = verify.verify(sigResult);

            MLog.d("Valid signature=" + valid);

            // Dismiss old dialog if it it showing
            txn.getActivity().runOnUiThread(() -> {
                if (currentPrompt != null) {
                    currentPrompt.dismiss();
                    currentPrompt.cancel();
                }
            });
            SignatureReq req = txn.sigReq;
            txn.signFuture.complete(new SigningResult(new MusapSignature(sigResult, req.getKey(), req.getAlgorithm(), req.getFormat())));

        } catch (Exception e) {
            txn.signFuture.complete(new SigningResult(new MusapException(e)));
            throw new MusapException(e);
        }
    }

    /**
     * Convert MUSAP Key type to YubiKey Key type
     * @param txn
     * @return
     */
    private KeyType resolveKeyType(SscdTransaction txn) {

        KeyAlgorithm algorithm = null;

        if (txn.keyGenReq != null) algorithm = txn.keyGenReq.getAlgorithm();

        if (algorithm == null) return KeyType.ECCP384;
        if (algorithm.isEc()) {
            if (algorithm.bits == 256) return KeyType.ECCP256;
            if (algorithm.bits == 384) return KeyType.ECCP384;
        } else if (algorithm.isRsa()) {
            if (algorithm.bits == 1024) return KeyType.RSA1024;
            if (algorithm.bits == 2048) return KeyType.RSA2048;
        }
        return KeyType.ECCP384;
    }

}
