package fi.methics.musap.sdk.api;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.discovery.KeyBindReq;
import fi.methics.musap.sdk.discovery.KeyDiscoveryAPI;
import fi.methics.musap.sdk.discovery.MetadataStorage;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;
import fi.methics.musap.sdk.util.MLog;

public class MUSAPClient {

    private static WeakReference<Context> context;
    private static KeyDiscoveryAPI keyDiscovery;
    private static MetadataStorage storage;

    public static void init(Context c) {
        context      = new WeakReference<>(c);
        keyDiscovery = new KeyDiscoveryAPI(c);
        storage      = new MetadataStorage(c);
    }

    /**
     * List SSCDs supported by this MUSAP library. To add an SSCD to this list, call {@link #enableSSCD(MUSAPSscdInterface)} first.
     * @return List of SSCDs that can be used to generate or bind keys
     */
    public static List<MUSAPSscdInterface> listEnabledSSCDS() {
        return keyDiscovery.listEnabledSscds();
    }

    /**
     * List active SSCDs that have user keys generated or bound
     * @return List of active SSCDs
     */
    public static List<MUSAPSscd> listActiveSSCDS() {
        return storage.listActiveSscds();
    }

    public static void enableSSCD(MUSAPSscdInterface sscd) {
        keyDiscovery.enableSSCD(sscd);
    }

    public static void bindKey(KeyBindReq req) {
        MetadataStorage storage = new MetadataStorage(context.get());
        storage.storeKeyMetaData(req);
    }

    /**
     * Generate a keypair and store the key metadata to MUSAP
     * @param sscd SSCD to generate the key with
     * @param req  Key Generation Request
     * @throws Exception
     */
    public static void generateKey(MUSAPSscdInterface sscd, KeyGenReq req) throws Exception {
        MUSAPKey key = sscd.generateKey(req);
        MetadataStorage storage = new MetadataStorage(context.get());
        storage.storeKey(key, sscd.getSscdInfo());
    }

    public static List<MUSAPKey> listKeys() {
        MetadataStorage storage = new MetadataStorage(context.get());
        List<MUSAPKey> keys = storage.listKeys();
        MLog.d("Found " + keys.size() + " keys");
        return keys;
    }

    public static MUSAPKey getKeyByUri(String keyUri) {
        MLog.d("Searching for key with KeyURI " + keyUri);
        MetadataStorage storage = new MetadataStorage(context.get());
        for (MUSAPKey key : storage.listKeys()) {
            if (key.getKeyUri().matches(new KeyURI(keyUri))) {
                MLog.d("Found key " + key.getKeyName());
                return key;
            }
        }
        MLog.d("Found no key!");
        return null;
    }

    public static MUSAPKey getKeyByUri(KeyURI keyUri) {
        MetadataStorage storage = new MetadataStorage(context.get());
        for (MUSAPKey key : storage.listKeys()) {
            if (key.getKeyUri().matches(keyUri)) {
                return key;
            }
        }
        return null;
    }
}
