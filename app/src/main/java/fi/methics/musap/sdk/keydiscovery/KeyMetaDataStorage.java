package fi.methics.musap.sdk.keydiscovery;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.util.MLog;

public class KeyMetaDataStorage {

    private static final String PREF_NAME = "musap";
    private static final String SSCD_SET  = "sscd";

    /**
     * Set that contains all known key names
     */
    private static final String KEY_NAME_SET = "keynames";

    /**
     * Prefix that storage uses to store key-speficic metadata.
     */
    private static final String KEY_JSON_PREFIX = "keyjson_";

    private Context context;

    public KeyMetaDataStorage(Context context) {
        this.context = context;
    }

    /**
     * Store a MUSAP key metadata.
     * The storage has two parts:
     *  1. A set of all known key names.
     *  2. For each key name, there is an entry "keyjson_<keyname>" that contains the key
     *     metadata.
     * @param key
     */
    public void storeKey(MUSAPKey key) {
        if (key == null) {
            MLog.e("Cannot store null MUSAP key");
            throw new IllegalArgumentException("Cannot store null MUSAP key");
        }
        if (key.getKeyName() == null) {
            MLog.e("Cannot store unnamed MUSAP key");
            throw new IllegalArgumentException("Cannot store unnamed MUSAP key");
        }

        MLog.d("Storing key");

        Set<String> oldKeyNames = this.getKeyNameSet();
        Set<String> newKeyNames = new HashSet<>(oldKeyNames);

        newKeyNames.add(key.getKeyName());

        String keyJson = new Gson().toJson(key);
        MLog.d("KeyJson=" + keyJson);

        this.getSharedPref()
                .edit()
                .putStringSet(KEY_NAME_SET, newKeyNames)
                .putString(this.makeStoreName(key), keyJson)
                .apply();
    }

    public List<MUSAPKey> listKeys() {
        Set<String> keyNames = this.getKeyNameSet();
        List<MUSAPKey> keyList = new ArrayList<>();
        for (String keyName: keyNames) {
            String keyJson = this.getKeyJson(keyName);
            if (keyJson == null) {
                MLog.e("Missing key metadata JSON for key name " + keyName);
            } else {
                MUSAPKey key = new Gson().fromJson(keyJson, MUSAPKey.class);
                keyList.add(key);
            }
        }

        return keyList;
    }

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

    private String makeStoreName(MUSAPKey key) {
        return KEY_JSON_PREFIX + key.getKeyName();
    }
    private String makeStoreName(String keyName) {
        return KEY_JSON_PREFIX + keyName;
    }

    private Set<String> getKeyNameSet() {
        return this.getSharedPref().getStringSet(KEY_NAME_SET, new HashSet<>());
    }

    private String getKeyJson(String keyName) {
        return this.getSharedPref().getString(this.makeStoreName(keyName), null);
    }

    private SharedPreferences getSharedPref() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
