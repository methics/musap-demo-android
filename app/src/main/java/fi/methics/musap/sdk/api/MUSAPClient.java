package fi.methics.musap.sdk.api;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fi.methics.musap.sdk.async.GenerateKeyTask;
import fi.methics.musap.sdk.async.SignTask;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.discovery.KeyBindReq;
import fi.methics.musap.sdk.discovery.KeyDiscoveryAPI;
import fi.methics.musap.sdk.discovery.MetadataStorage;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;
import fi.methics.musap.sdk.sign.MUSAPSignature;
import fi.methics.musap.sdk.sign.SignatureReq;
import fi.methics.musap.sdk.util.MLog;
import fi.methics.musap.sdk.util.MusapAsyncTask;
import fi.methics.musap.sdk.util.MusapCallback;

public class MUSAPClient {

    private static WeakReference<Context> context;
    private static KeyDiscoveryAPI keyDiscovery;
    private static MetadataStorage storage;
    private static Executor executor;

    public static void init(Context c) {
        context      = new WeakReference<>(c);
        keyDiscovery = new KeyDiscoveryAPI(c);
        storage      = new MetadataStorage(c);
        executor     = new ThreadPoolExecutor(2, 5, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(5));
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
    public static void generateKey(MUSAPSscdInterface sscd, KeyGenReq req, MusapCallback<MUSAPKey> callback) {
        new GenerateKeyTask(callback, context.get(), sscd, req).executeOnExecutor(executor);
    }

    public static void sign(MUSAPSscdInterface sscd, SignatureReq req, MusapCallback<MUSAPSignature> callback) {
        new SignTask(callback, context.get(), sscd, req).executeOnExecutor(executor);
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
