package fi.methics.musap.sdk.yubikey;

import android.app.Activity;
import android.content.Context;
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

import java.security.KeyPairGenerator;
import java.security.Security;

import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.extension.SscdSettings;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;
import fi.methics.musap.sdk.util.MLog;

public class YubiKeyExtension implements MUSAPSscdInterface<YubiKeySettings> {


    private static final byte[] MANAGEMENT_KEY = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final ManagementKeyType TYPE = ManagementKeyType.TDES;
    private static final char[] DEFAULT_PIN = "123456".toCharArray();

    private YubiKeySettings settings = new YubiKeySettings();

    /**
     * PIN
     */
    private final char[] pin;
    private final ManagementKeyType type;

    private final byte[] managementKey;

    private final YubiKitManager yubiKitManager;

    private final Activity activity;
    private final Context c;

    public YubiKeyExtension(Activity activity) {
        this.managementKey = MANAGEMENT_KEY;
        this.pin = DEFAULT_PIN;
        this.type = TYPE;
        this.c = activity.getBaseContext();
        this.activity = activity;
        this.yubiKitManager = new YubiKitManager(this.c);
    }

    @Override
    public MUSAPKey bindKey(KeyBindReq req) throws Exception {
        // Bind an existing YubiKey keypair to MUSAP by signing with it
        // Get the public key, and verify that it matches 
        return null;
    }

    @Override
    public MUSAPKey generateKey(KeyGenReq req) throws Exception {
        try {
            yubiKitManager.startNfcDiscovery(new NfcConfiguration(), this.activity, device -> {
                MLog.d("Found NFC");
                connect(device, req);
            });
        } catch (NfcNotAvailable e) {
            if (e.isDisabled()) {
                Toast.makeText(c, "NFC is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "NFC is not available", Toast.LENGTH_SHORT).show();
            }
        }

        return null;
    }

    @Override
    public MUSAPSscd getSscdInfo() {
        return null;
    }

    @Override
    public boolean isKeygenSupported() {
        return true;
    }

    @Override
    public YubiKeySettings getSettings() {
        return settings;
    }

    private void connect(final NfcYubiKeyDevice device, final KeyGenReq req)  {
        device.requestConnection(SmartCardConnection.class, result -> {
            // The result is a Result<SmartCardConnection, IOException>, which represents either a successful connection, or an error.
            try {
                SmartCardConnection connection = result.getValue();  // This may throw an IOException
                PivSession pivSession = new PivSession(connection);

                pivSession.authenticate(this.type, this.managementKey);

                PivProvider pivProvider = new PivProvider(pivSession);
                Security.insertProviderAt(pivProvider, 1); // JCA Security providers are indexed from 1

                KeyPairGenerator ecKpg = KeyPairGenerator.getInstance("YKPivEC");
                MLog.d("Initialized KeyPairGenerator");

                // PinPolicy and TouchPolicy should come from the using app
                // Pin and used slot comes from the user

                ecKpg.initialize(
                        new PivAlgorithmParameterSpec(
                                Slot.AUTHENTICATION,
                                this.resolveKeyType(req),
                                null, // PinPolicy
                                null, // TouchPolicy
                                this.pin // PIV PIN
                        )
                );
                ecKpg.generateKeyPair();

                MLog.d("Generated KeyPair");

            } catch(Exception e) {
                MLog.e("Failed to connect", e);
            }
        });
    }

    /**
     * Convert MUSAP Key type to YubiKey Key type
     * @param req
     * @return
     */
    private KeyType resolveKeyType(KeyGenReq req) {
        // TODO: Implement

        switch (req.getType()) {
        }

        return KeyType.ECCP384;
    }
}
