package fi.methics.musap.sdk.api;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryAPI;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryCriteria;
import fi.methics.musap.sdk.keydiscovery.KeyMetaDataStorage;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keygeneration.KeygenAPI;
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

    public static List<KeyURI> listMatchingMethods(Map<KeyDiscoveryCriteria, String> criteria) {
        KeyDiscoveryAPI api = new KeyDiscoveryAPI(context.get());

        return api.listMatchingMethods(criteria);
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

    @Deprecated
    public static void generateKey(KeyGenReq req) {
        MUSAPKey key = new KeygenAPI().generateKey(req);
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        storage.storeKey(key);
    }

    @Deprecated
    public static Set<String> listKeyNames() {
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        return storage.listKeyNames();
    }

    public static List<MUSAPKey> listKeys() {
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        List<MUSAPKey> keys = storage.listKeys();
        MLog.d("Found " + keys.size() + " keys");
        return keys;
    }

    /**
     * Get a MUSAP key by keyname.
     * This is not ideal way to get a key, but easy for now.
     * In the future, get key by KeyURI instead.
     * @param keyName
     * @return
     */
    public static MUSAPKey getKeyByName(String keyName) {
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        return storage.getKeyMetadata(keyName);
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
