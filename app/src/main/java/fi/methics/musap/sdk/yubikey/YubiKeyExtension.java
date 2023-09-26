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
import java.util.Date;

import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;
import fi.methics.musap.sdk.sign.MUSAPSignature;
import fi.methics.musap.sdk.sign.SignatureReq;
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
    public MUSAPSignature sign(SignatureReq req) throws Exception {
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

                final Slot usedSlot = Slot.SIGNATURE;

                ecKpg.initialize(
                        new PivAlgorithmParameterSpec(
                                usedSlot,
                                this.resolveKeyType(req),
                                null, // PinPolicy
                                null, // TouchPolicy
                                this.pin // PIV PIN
                        )
                );
                KeyPair keyPair= ecKpg.generateKeyPair();

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

                MLog.d("Put certificate to slot");

            } catch(Exception e) {
                MLog.e("Failed to connect", e);
            }
        });
    }

    public void sign() {
        try {
            yubiKitManager.startNfcDiscovery(new NfcConfiguration(), this.activity, device -> {
                MLog.d("Found NFC");
                yubiSign(device);
            });
        } catch (Exception e) {
            MLog.e("Failed to sign", e);
        }
    }

    private void yubiSign(NfcYubiKeyDevice device) {
        device.requestConnection(SmartCardConnection.class, result -> {
            try {
                String msg = "Test string";

                SmartCardConnection connection = result.getValue();  // This may throw an IOException
                PivSession pivSession = new PivSession(connection);

                pivSession.authenticate(this.type, this.managementKey);

                Slot slot = Slot.SIGNATURE;

                PivProvider pivProvider = new PivProvider(pivSession);
                KeyStore keyStore = KeyStore.getInstance("YKPiv", pivProvider);

                keyStore.load(null);

                PublicKey publicKey = keyStore.getCertificate(slot.getStringAlias()).getPublicKey();
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(slot.getStringAlias(), this.pin);

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

            } catch(Exception e) {
                MLog.e("Failed to sign", e);
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

//        switch (req.getType()) {
//        }

        return KeyType.ECCP384;
    }
}
