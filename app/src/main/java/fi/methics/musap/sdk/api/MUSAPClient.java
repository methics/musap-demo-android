package fi.methics.musap.sdk.api;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryAPI;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryCriteria;
import fi.methics.musap.sdk.keydiscovery.KeyMetaDataStorage;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keygeneration.KeygenAPI;
import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;

public class MUSAPClient {

    private static WeakReference<Context> context;

    public static void init(Context c) {
        context = new WeakReference<>(c);
    }

    public List<KeyURI> listMatchingMethods(Map<KeyDiscoveryCriteria, String> criteria) {
        KeyDiscoveryAPI api = new KeyDiscoveryAPI(context.get());

        return api.listMatchingMethods(criteria);
    }

    public void bindKey(KeyBindReq req) {
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        storage.storeKeyMetaData(req);
    }

    public void generateKey(KeyGenReq req) {
        MUSAPKey key = new KeygenAPI().generateKey(req);
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        storage.storeKey(key);
    }

    public Set<String> listKeyNames() {
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        return storage.listKeyNames();
    }

    /**
     * Get a MUSAP key by keyname.
     * This is not ideal way to get a key, but easy for now.
     * In the future, get key by KeyURI instead.
     * @param keyName
     * @return
     */
    public MUSAPKey getKeyByName(String keyName) {
        KeyMetaDataStorage storage = new KeyMetaDataStorage(context.get());
        return storage.getKeyMetadata(keyName);
    }

    public MUSAPKey getKeyByUri(String keyUri) {
        // TODO: Getting a key by URI is important because passing MUSAPKey object
        //  between activities/fragements is harder than passing strings
        return null;
    }

    public MUSAPKey getKeyByUri(KeyURI keyUri) {
        // TODO: Getting a key by URI is important because passing MUSAPKey object
        //  between activities/fragements is harder than passing strings
        return null;
    }
}
