package fi.methics.musap.sdk.api;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryAPI;
import fi.methics.musap.sdk.keydiscovery.KeyMetaDataStorage;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.util.MLog;

public class MUSAPClient {

    private static WeakReference<Context> context;
    private static KeyDiscoveryAPI keyDiscovery;

    public static void init(Context c) {
        context = new WeakReference<>(c);
        keyDiscovery = new KeyDiscoveryAPI(c);
    }

    public static List<MUSAPSscdInterface> listSSCDS() {
        return keyDiscovery.listSscds();
    }

    public static void enableSSCD(MUSAPSscdInterface sscd) {
        keyDiscovery.enableSSCD(sscd);
    }

    public static void bindKey(KeyBindReq req) {
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
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
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        storage.storeKey(key);
    }

    public static List<MUSAPKey> listKeys() {
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        List<MUSAPKey> keys = storage.listKeys();
        MLog.d("Found " + keys.size() + " keys");
        return keys;
    }

    public static MUSAPKey getKeyByUri(String keyUri) {
        MLog.d("Searching for key with KeyURI " + keyUri);
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
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
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        for (MUSAPKey key : storage.listKeys()) {
            if (key.getKeyUri().matches(keyUri)) {
                return key;
            }
        }
        return null;
    }
}
