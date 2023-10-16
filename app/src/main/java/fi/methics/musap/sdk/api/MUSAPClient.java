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
import fi.methics.musap.sdk.discovery.KeySearchReq;
import fi.methics.musap.sdk.discovery.SscdSearchReq;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.discovery.KeyBindReq;
import fi.methics.musap.sdk.discovery.KeyDiscoveryAPI;
import fi.methics.musap.sdk.discovery.MetadataStorage;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;
import fi.methics.musap.sdk.keyuri.MusapLink;
import fi.methics.musap.sdk.keyuri.MusapMessage;
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
     * Generate a keypair and store the key metadata to MUSAP
     * @param sscd SSCD to generate the key with
     * @param req  Key Generation Request
     * @param callback Callback that will deliver success or failure
     */
    public static void generateKey(MUSAPSscdInterface sscd, KeyGenReq req, MusapCallback<MUSAPKey> callback) {
        new GenerateKeyTask(callback, context.get(), sscd, req).executeOnExecutor(executor);
    }

    /**
     * Bind a keypair and store the key metadata to MUSAP
     * @param sscd SSCD to bind the key with
     * @param req  Key Bind Request
     * @param callback Callback that will deliver success or failure
     */
    public static void bindKey(MUSAPSscdInterface sscd, KeyGenReq req, MusapCallback<MUSAPKey> callback) {
        // TODO: Change to KeyBindTask, etc
        new GenerateKeyTask(callback, context.get(), sscd, req).executeOnExecutor(executor);
    }

    /**
     * Sign data with given SSCD
     * @param sscd SSCD to sign with
     * @param req  Request containing the data to sign
     * @param callback Callback that will deliver success or failure
     */
    public static void sign(MUSAPSscdInterface sscd, SignatureReq req, MusapCallback<MUSAPSignature> callback) {
        new SignTask(callback, context.get(), sscd, req).executeOnExecutor(executor);
    }


    /**
     * List SSCDs supported by this MUSAP library. To add an SSCD to this list, call {@link #enableSSCD(MUSAPSscdInterface)} first.
     * @return List of SSCDs that can be used to generate or bind keys
     */
    public static List<MUSAPSscdInterface> listEnabledSSCDS() {
        return keyDiscovery.listEnabledSscds();
    }

    /**
     * List SSCDs supported by this MUSAP library. To add an SSCD to this list, call {@link #enableSSCD(MUSAPSscdInterface)} first.
     * @param req Search request that filters the output
     * @return List of SSCDs that can be used to generate or bind keys
     */
    public static List<MUSAPSscdInterface> listEnabledSSCDS(SscdSearchReq req) {
        // TODO: Filter based on the req
        return keyDiscovery.listEnabledSscds();
    }

    /**
     * List active SSCDs that have user keys generated or bound
     * @return List of active SSCDs
     */
    public static List<MUSAPSscd> listActiveSSCDS() {
        return storage.listActiveSscds();
    }

    /**
     * List all available keys
     * @return List of keys
     */
    public static List<MUSAPKey> listKeys() {
        MetadataStorage storage = new MetadataStorage(context.get());
        List<MUSAPKey> keys = storage.listKeys();
        MLog.d("Found " + keys.size() + " keys");
        return keys;
    }

    /**
     * List all keys that match the given search paramters
     * @param req Search request that filters the output
     * @return matching keys
     */
    public static List<MUSAPKey> listKeys(KeySearchReq req) {
        // TODO: Filter keys by req
        return listKeys();
    }


    /**
     * Enable an SSCD. This needs to be called for each SSCD that the application using MUSAP wants
     * to support. These will be searchable with {@link #listEnabledSSCDS()}}.
     * @param sscd SSCD to enable
     */
    public static void enableSSCD(MUSAPSscdInterface sscd) {
        keyDiscovery.enableSSCD(sscd);
    }

    @Deprecated // Remove this and use bindKey(sscd, req, callback)
    public static void bindKey(KeyBindReq req) {
        MetadataStorage storage = new MetadataStorage(context.get());
        storage.storeKeyMetaData(req);
    }

    /**
     * Get a key by KeyURI
     * @param keyUri KeyURI as String
     * @return Key or null if none found
     */
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

    /**
     * Get a key by KeyURI
     * @param keyUri KeyURI as {@link KeyURI} object
     * @return Key or null if none found
     */
    public static MUSAPKey getKeyByUri(KeyURI keyUri) {
        MetadataStorage storage = new MetadataStorage(context.get());
        for (MUSAPKey key : storage.listKeys()) {
            if (key.getKeyUri().matches(keyUri)) {
                return key;
            }
        }
        return null;
    }

    /**
     * Import MUSAP key data and SSCD details
     * @param data JSON data from another MUSA
     */
    public static void importData(String data) {
        // TODO
    }

    /**
     * Export MUSAP key data and SSCD details
     * @return JSON export that can be imported in another MUSAP
     */
    public static String exportData() {
        return "";
    }

    /**
     * Remove a key from MUSAP.
     * @param key key to remove
     */
    public static void removeKey(MUSAPKey key) {
        // TODO
    }

    /**
     * Remove an active SSCD from MUSAP.
     * @param sscd SSCD to remove
     */
    public static void removeSscd(MUSAPSscd sscd) {
        // TODO
    }

    /**
     * Enable a MUSAP Link connection.
     * Enabling allows the MUSAP Link to securely request signatures from this MUSAP.
     * @param url URL of the MUSAP link service
     */
    public static MusapLink enableLink(String url) {
        // TODO
        return new MusapLink(url, null);
    }

    /**
     * Disable MUSAP link connection
     */
    public static void disableLink() {
        // TODO
    }

    /**
     * Check if MUSAP Link has been enabled
     * @return true if enabled
     */
    public static boolean isMusapLinkEnabled() {
        return false; // TODO
    }

    /**
     * Poll MUSAP Link for an incoming signature request. This should be called periodically and/or
     * when a notification wakes up the application.
     * @return SignatureReq or null if no request available
     * @throws MUSAPException if polling failed (e.g. a network issue)
     */
    public static SignatureReq pollForSignatureRequest() {
        return null;
    }

}
