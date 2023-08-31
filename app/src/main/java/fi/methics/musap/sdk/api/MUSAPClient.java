package fi.methics.musap.sdk.api;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryAPI;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryCriteria;
import fi.methics.musap.sdk.keydiscovery.KeyMetaDataStorage;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keygeneration.KeygenAPI;
import fi.methics.musap.sdk.keyuri.KeyURI;

public class MUSAPClient {

    private static WeakReference<Context> context;

    public static void init(Context c) {
        context = new WeakReference<>(c);
    }

    public List<KeyURI> listMatchingMethods(Map<KeyDiscoveryCriteria, String> criteria) {
        KeyDiscoveryAPI api = new KeyDiscoveryAPI();

        return api.listMatchingMethods(criteria);
    }

    public void bindKey(KeyBindReq req) {
        KeyMetaDataStorage storage = new KeyMetaDataStorage();
        storage.storeKeyMetaData(context.get(), req);
    }

    public void generateKey(KeyGenReq req) {
        new KeygenAPI().generateKey(req);
    }
}
