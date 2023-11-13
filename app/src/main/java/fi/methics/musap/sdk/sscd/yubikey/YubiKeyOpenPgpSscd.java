package fi.methics.musap.sdk.sscd.yubikey;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.yubico.yubikit.android.YubiKitManager;
import com.yubico.yubikit.android.transport.nfc.NfcConfiguration;
import com.yubico.yubikit.android.transport.nfc.NfcNotAvailable;
import com.yubico.yubikit.android.transport.nfc.NfcYubiKeyDevice;
import com.yubico.yubikit.core.smartcard.SmartCardConnection;
import com.yubico.yubikit.openpgp.KeyRef;
import com.yubico.yubikit.openpgp.OpenPgpCurve;
import com.yubico.yubikit.openpgp.OpenPgpSession;
import com.yubico.yubikit.piv.ManagementKeyType;
import com.yubico.yubikit.piv.PivSession;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import fi.methics.musap.R;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.internal.datatype.KeyAlgorithm;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.MusapSignature;
import fi.methics.musap.sdk.internal.datatype.MusapSscd;
import fi.methics.musap.sdk.internal.datatype.SignatureFormat;
import fi.methics.musap.sdk.internal.discovery.KeyBindReq;
import fi.methics.musap.sdk.internal.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.internal.sign.SignatureReq;
import fi.methics.musap.sdk.internal.util.KeyGenerationResult;
import fi.methics.musap.sdk.internal.util.MLog;
import fi.methics.musap.sdk.internal.util.SigningResult;

public class YubiKeyOpenPgpSscd implements MusapSscdInterface<YubiKeySettings> {
    private static final byte[] MANAGEMENT_KEY = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final ManagementKeyType TYPE = ManagementKeyType.TDES;


    public static final char[] DEFAULT_USER_PIN = "123456".toCharArray();
    public static final char[] DEFAULT_ADMIN_PIN = "12345678".toCharArray();

    private static final String SSCD_TYPE        = "YubikeyEddsa";
    private static final String ATTRIBUTE_SERIAL = "SerialNumber";

    private YubiKeySettings settings = new YubiKeySettings();

    private AlertDialog currentPrompt;
    private CompletableFuture<KeyGenerationResult> keygenFuture;
    private CompletableFuture<SigningResult> signFuture;

    private final ManagementKeyType type;

    private final byte[] managementKey;

    private final YubiKitManager yubiKitManager;

    private KeyGenReq keyGenReq;
    private SignatureReq sigReq;
    private GenerateKeyCallback callback;

    private final Context c;

    public YubiKeyOpenPgpSscd(Context context) {
        this.managementKey = MANAGEMENT_KEY;
        this.type = TYPE;
        this.c = context;
        this.yubiKitManager = new YubiKitManager(this.c);
    }

    @Override
    public MusapKey bindKey(KeyBindReq req) throws Exception {
        return null;
    }

    @Override
    public MusapKey generateKey(KeyGenReq req) throws Exception {

        this.keyGenReq = req;
        this.sigReq = null;
        this.keygenFuture = new CompletableFuture<>();

        Context c = req.getActivity();
        View v = LayoutInflater.from(c).inflate(R.layout.dialog_pin, null);

        String pin = "123456";

        this.yubiKeyGen(pin, req, null);

        return null;
    }

    private void yubiKeyGen(String pin, KeyGenReq req, GenerateKeyCallback callback) {
        try {
            yubiKitManager.startNfcDiscovery(new NfcConfiguration(), req.getActivity(), device -> {
                MLog.d("Found NFC");
                connect(device, req, pin);
            });
        } catch (NfcNotAvailable e) {
            if (e.isDisabled()) {
                Toast.makeText(c, "NFC is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "NFC is not available", Toast.LENGTH_SHORT).show();
            }
            yubiKitManager.stopNfcDiscovery(req.getActivity());
        }
    }

    private void connect(final NfcYubiKeyDevice device, final KeyGenReq req, String pin)  {

        device.requestConnection(SmartCardConnection.class, result -> {
            // The result is a Result<SmartCardConnection, IOException>, which represents either a successful connection, or an error.
            try {
                boolean success = result.isSuccess();
                MLog.d("Connection successful=" + success);

                // If the connection is not successful, try again
                if (!success) {
                    MLog.d("Failed to connect");
                    this.showKeyGenFailedDualog(req);
                } else {
                    MLog.d("PIN=" + pin);
                    keyGenOnDevice(req, pin, result.getValue());
                }
            } catch (Exception e) {
                MLog.e("Failed to connect", e);
                this.showKeyGenFailedDualog(req);
                yubiKitManager.stopNfcDiscovery(req.getActivity());
            }
        });
    }

    private void keyGenOnDevice(KeyGenReq req, String pin, SmartCardConnection connection) throws Exception {
        OpenPgpSession openpgp = new OpenPgpSession(connection);
        MLog.d("Opened OpenPGP session");

        MLog.d("Device supports ECC=" + openpgp.supports(OpenPgpSession.FEATURE_EC_KEYS));

        Security.removeProvider("BC");
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        openpgp.verifyAdminPin(DEFAULT_ADMIN_PIN);


        byte[] message = "hello".getBytes(StandardCharsets.UTF_8);
        PublicKey publicKey = openpgp.generateEcKey(KeyRef.SIG, OpenPgpCurve.Ed25519).toPublicKey();
        openpgp.verifyUserPin(DEFAULT_USER_PIN, false);
        byte[] signature = openpgp.sign(message);

        Signature verifier = Signature.getInstance("Ed25519");
        verifier.initVerify(publicKey);
        verifier.update(message);
        MLog.d("Signature valid=" + verifier.verify(signature));
    }

    public void showKeyGenFailedDualog(KeyGenReq req) {

        // Dismiss old dialog if it it showing
        req.getActivity().runOnUiThread(() -> {
            if (currentPrompt != null) {
                currentPrompt.dismiss();
            }
        });

        Context c = req.getActivity();
        View v = LayoutInflater.from(c).inflate(R.layout.dialog_keygen_failed, null);

        req.getActivity().runOnUiThread(() -> {
            currentPrompt = new AlertDialog.Builder(c)
                    .setTitle("Key Generation Failed")
                    .setView(v)
                    .create();
            currentPrompt.show();
        });
    }

        @Override
    public MusapSignature sign(SignatureReq req) throws Exception {
        return null;
    }

    @Override
    public MusapSscd getSscdInfo() {
        return new MusapSscd.Builder()
                .setSscdName("Yubikey OpenPGP")
                .setSscdType(SSCD_TYPE)
                .setCountry("FI")
                .setProvider("Yubico")
                .setKeygenSupported(true)
                .setSupportedAlgorithms(Arrays.asList(KeyAlgorithm.ECC_P256_K1, KeyAlgorithm.ECC_P384_K1))
                .setSupportedFormats(Arrays.asList(SignatureFormat.RAW))
                .build();
    }

    @Override
    public String generateSscdId(MusapKey key) {
        return SSCD_TYPE + "/" + key.getAttributeValue(ATTRIBUTE_SERIAL);
    }

    @Override
    public boolean isKeygenSupported() {
        return true;
    }

    @Override
    public YubiKeySettings getSettings() {
        return this.settings;
    }
}
