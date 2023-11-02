package fi.methics.musap.sdk.internal.discovery;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.MusapSscd;
import fi.methics.musap.sdk.internal.util.MLog;

/**
 * MUSAP Metadata Storage class
 * This is used to store Key and SSCD metadata.
 */
public class MetadataStorage {

    private static final String PREF_NAME = "musap";
    private static final String SSCD_SET  = "sscd";

    /**
     * Set that contains all known key names
     */
    private static final String KEY_NAME_SET = "keynames";
    private static final String SSCD_ID_SET  = "sscdids";

    /**
     * Prefix that storage uses to store key-speficic metadata.
     */
    private static final String KEY_JSON_PREFIX  = "keyjson_";
    private static final String SSCD_JSON_PREFIX = "sscdjson_";

    private Context context;

    public MetadataStorage(Context context) {
        this.context = context;
    }

    /**
     * Store a MUSAP key metadata.
     * The storage has two parts:
     *  1. A set of all known key names.
     *  2. For each key name, there is an entry "keyjson_<keyname>" that contains the key
     *     metadata.
     * @param key  MUSAP key
     * @param sscd MUSAP SSCD that holds the key
     */
    public void storeKey(MusapKey key, MusapSscd sscd) {
        if (key == null) {
            MLog.e("Cannot store null MUSAP key");
            throw new IllegalArgumentException("Cannot store null MUSAP key");
        }
        if (key.getKeyName() == null) {
            MLog.e("Cannot store unnamed MUSAP key");
            throw new IllegalArgumentException("Cannot store unnamed MUSAP key");
        }

        MLog.d("Storing key");

        // Update Key Name list with new Key Name
        Set<String> newKeyNames = new HashSet<>(this.getKeyNameSet());
        newKeyNames.add(key.getKeyName());

        String keyJson = new Gson().toJson(key);
        MLog.d("KeyJson=" + keyJson);

        this.getSharedPref()
                .edit()
                .putStringSet(KEY_NAME_SET, newKeyNames)
                .putString(this.makeStoreName(key), keyJson)
                .apply();

        if (sscd != null) {
            this.storeSscd(sscd);
        }
    }

    /**
     * List available MUSAP keys
     * @return MUSAP keys
     */
    public List<MusapKey> listKeys() {
        Set<String> keyNames = this.getKeyNameSet();
        List<MusapKey> keyList = new ArrayList<>();
        for (String keyName: keyNames) {
            String keyJson = this.getKeyJson(keyName);
            if (keyJson == null) {
                MLog.e("Missing key metadata JSON for key name " + keyName);
            } else {
                MusapKey key = new Gson().fromJson(keyJson, MusapKey.class);
                keyList.add(key);
            }
        }

        return keyList;
    }

    /**
     * List available MUSAP keys that match the search request.
     * @param req Key search request
     * @return List of matching keys
     */
    public List<MusapKey> listKeys(KeySearchReq req) {
        Set<String> keyNames = this.getKeyNameSet();
        List<MusapKey> keyList = new ArrayList<>();
        for (String keyName: keyNames) {
            String keyJson = this.getKeyJson(keyName);
            if (keyJson == null) {
                MLog.e("Missing key metadata JSON for key name " + keyName);
            } else {
                MusapKey key = new Gson().fromJson(keyJson, MusapKey.class);
                if (req.matches(key)) {
                    MLog.d("Request matches key " + keyName);
                    keyList.add(key);
                } else {
                    MLog.d("Request does not match key " + keyName);
                }
            }
        }

        return keyList;
    }

    /**
     * Remove key metadata from storage
     * @param key Key to remove
     * @return true if key was found and removed
     */
    public boolean removeKey(MusapKey key) {
        // Update Key Name list without given Key Name
        Set<String> newKeyNames = new HashSet<>(this.getKeyNameSet());
        if (!this.getKeyNameSet().contains(key.getKeyName())) {
            MLog.d("No key found with name " + key.getKeyName());
            return false;
        }
        newKeyNames.remove(key.getKeyName());

        String keyJson = new Gson().toJson(key);
        MLog.d("KeyJson=" + keyJson);

        this.getSharedPref()
                .edit()
                .putStringSet(KEY_NAME_SET, newKeyNames)
                .putString(this.makeStoreName(key), keyJson)
                .remove(this.makeStoreName(key.getKeyName()))
                .apply();
        return true;
    }

    /**
     * Store metadata of an active MUSAP SSCD
     * @param sscd SSCD (that has keys bound or generated)
     */
    public void storeSscd(MusapSscd sscd) {
        if (sscd == null) {
            MLog.e("Cannot store null MUSAP SSCD");
            throw new IllegalArgumentException("Cannot store null MUSAP SSCD");
        }
        if (sscd.getSscdId() == null) {
            MLog.e("Cannot store MUSAP SSCD without an ID");
            throw new IllegalArgumentException("Cannot store MUSAP SSCD without an ID");
        }

        // Update SSCD id list with new SSCD ID
        Set<String> sscdIds = new HashSet<>(this.getSscdIdSet());
        if (sscdIds.contains(sscd.getSscdId())) {
            MLog.d("SSCD " + sscd.getSscdId() + " already stored");
            return;
        }
        sscdIds.add(sscd.getSscdId());

        MLog.d("Storing SSCD");

        String json = new Gson().toJson(sscd);
        MLog.d("SSCD JSON=" + json);

        this.getSharedPref()
                .edit()
                .putStringSet(SSCD_ID_SET, sscdIds)
                .putString(this.makeStoreName(sscd), json)
                .apply();
    }


    /**
     * List available active MUSAP SSCDs
     * @return active MUSAP SSCDs (that have keys bound or generated)
     */
    public List<MusapSscd> listActiveSscds() {
        Set<String> sscdIds = this.getSscdIdSet();
        List<MusapSscd> sscdList = new ArrayList<>();
        for (String sscdid : sscdIds) {
            String sscdJson = this.getSscdJson(sscdid);
            if (sscdJson == null) {
                MLog.e("Missing SSCD metadata JSON for SSCD ID " + sscdid);
            } else {
                MusapSscd sscd = new Gson().fromJson(sscdJson, MusapSscd.class);
                sscdList.add(sscd);
            }
        }
        return sscdList;
    }

    /**
     * Store MUSAP import data
     * @param data import data
     */
    public void storeImportData(MusapImportData data) {
        if (data == null) return;
        List<MusapSscd> activeSscds = this.listActiveSscds();
        List<MusapSscdInterface> enabledSscds = MusapClient.listEnabledSscds();
        List<MusapKey>  activeKeys  = this.listKeys();
        for (MusapSscd sscd : data.sscds) {
            // Avoid duplicate SSCDs and SSCDs that are not enabled in this MUSAP
            boolean alreadyExists   = activeSscds.stream().anyMatch(s -> s.getSscdId().equals(sscd.getSscdId()));
            boolean sscdTypeEnabled = !enabledSscds.stream().anyMatch(s -> s.getSscdInfo().getSscdType().equals(sscd.getSscdType()));
            if (alreadyExists || !sscdTypeEnabled) continue;
            this.storeSscd(sscd);
        }
        for (MusapKey key : data.keys) {
            // Avoid duplicate keys
            if (activeKeys.stream().anyMatch(k -> k.getKeyUri().equals(k.getKeyUri()))) continue;
            if (key.getSscd() != null) {
                this.storeKey(key, key.getSscd().getSscdInfo());
            }
        }
    }

    /**
     * Get MUSAP import data for export
     * @return import data
     */
    public MusapImportData getImportData() {
        MusapImportData data = new MusapImportData();
        data.sscds = this.listActiveSscds();
        data.keys  = this.listKeys();
        return data;
    }

    @Deprecated
    public void storeKeyMetaData(KeyBindReq req) {
        Set<String> metadatas = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                        .getStringSet(SSCD_SET, new HashSet<>());
        Set<String> newMetadatas = new HashSet<>(metadatas);
        newMetadatas.add(req.getSscd());

        this.getSharedPref()
                .edit()
                .putStringSet(SSCD_SET, newMetadatas)
                .apply();
    }

    private String makeStoreName(MusapKey key) {
        return KEY_JSON_PREFIX + key.getKeyName();
    }
    private String makeStoreName(MusapSscd sscd) {
        return SSCD_JSON_PREFIX + sscd.getSscdId();
    }
    private String makeStoreName(String keyName) {
        return KEY_JSON_PREFIX + keyName;
    }

    private Set<String> getKeyNameSet() {
        return this.getSharedPref().getStringSet(KEY_NAME_SET, new HashSet<>());
    }
    private Set<String> getSscdIdSet() {
        return this.getSharedPref().getStringSet(SSCD_ID_SET, new HashSet<>());
    }

    private String getKeyJson(String keyName) {
        return this.getSharedPref().getString(this.makeStoreName(keyName), null);
    }
    private String getSscdJson(String sscdid) {
        return this.getSharedPref().getString(SSCD_JSON_PREFIX + sscdid, null);
    }
    private SharedPreferences getSharedPref() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
