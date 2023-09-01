package fi.methics.musap.sdk.keydiscovery;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.util.MLog;

public class KeyMetaDataStorage {

    private static final String PREF_NAME = "musap";
    private static final String SSCD_SET = "sscd";

    /**
     * Set that contains all known key names
     */
    private static final String KEY_NAME_SET = "keynames";

    /**
     * Prefix that storage uses to store key-speficic metadata.
     */
    private static final String KEY_JSON_PREFIX = "keyjson_";

    public KeyMetaDataStorage() {
    }

    /**
     * Store a MUSAP key metadata.
     * The storage has two parts:
     *  1. A set of all known key names.
     *  2. For each key name, there is an entry "keyjson_<keyname>" that contains the key
     *     metadata.
     * @param context
     * @param key
     */
    public void storeKey(Context context, MUSAPKey key) {
        if (key == null) {
            MLog.e("Cannot store null MUSAP key");
            throw new IllegalArgumentException("Cannot store null MUSAP key");
        }
        if (key.getKeyName() == null) {
            MLog.e("Cannot store unnamed MUSAP key");
            throw new IllegalArgumentException("Cannot store unnamed MUSAP key");
        }

        Set<String> oldKeyNames = this.getKeyNameSet(context);
        Set<String> newKeyNames = new HashSet<>(oldKeyNames);

        newKeyNames.add(key.getKeyName());

        String keyJson = new Gson().toJson(key);
        MLog.d("KeyJson=" + keyJson);

        this.getSharedPref(context)
                .edit()
                .putStringSet(KEY_NAME_SET, newKeyNames)
                .putString(this.makeStoreName(key), keyJson)
                .apply();
    }

    public List<MUSAPKey> listKeys(Context context) {
        Set<String> keyNames = this.getKeyNameSet(context);
        List<MUSAPKey> keyList = new ArrayList<>();
        for (String keyName: keyNames) {
            String keyJson = this.getKeyJson(context, keyName);
            if (keyJson == null) {
                MLog.e("Missing key metadata JSON for key name " + keyName);
            } else {
                MUSAPKey key = new Gson().fromJson(keyJson, MUSAPKey.class);
                keyList.add(key);
            }
        }

        return keyList;
    }


    public void storeKeyMetaData(Context context, KeyBindReq req) {
        Set<String> metadatas = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                        .getStringSet(SSCD_SET, new HashSet<>());
        Set<String> newMetadatas = new HashSet<>(metadatas);
        newMetadatas.add(req.getSscd());

        this.getSharedPref(context)
                .edit()
                .putStringSet(SSCD_SET, newMetadatas)
                .apply();
    }

    public MUSAPKey getKeyMetadata(Context context, String keyName) {
        String keyJson = this.getKeyJson(context, keyName);
        return new Gson().fromJson(keyJson, MUSAPKey.class);
    }


    private String makeStoreName(MUSAPKey key) {
        return KEY_JSON_PREFIX + key.getKeyName();
    }
    private String makeStoreName(String keyName) {
        return KEY_JSON_PREFIX + keyName;
    }

    private Set<String> getKeyNameSet(Context c) {
        return this.getSharedPref(c).getStringSet(KEY_NAME_SET, new HashSet<>());
    }

    private String getKeyJson(Context c, String keyName) {
        return this.getSharedPref(c).getString(this.makeStoreName(keyName), null);
    }

    private SharedPreferences getSharedPref(Context c) {
        return c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
