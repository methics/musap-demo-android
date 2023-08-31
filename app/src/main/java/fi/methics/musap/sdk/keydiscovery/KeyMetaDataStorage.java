package fi.methics.musap.sdk.keydiscovery;

import android.content.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.methics.musap.sdk.keyuri.MUSAPKey;

public class KeyMetaDataStorage {

    private static final String PREF_NAME = "musap";
    private static final String SSCD_SET = "sscd";

    public KeyMetaDataStorage() {
    }

    //public void storeKey(Context context, MUSAPKey key) {
    //    Set<String> metadatas = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    //            .getStringSet(SSCD_SET, new HashSet<>());
    //    Set<String> newMetadatas = new HashSet<>(metadatas);
    //    newMetadatas.add(req.getSscd());
//
    //    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    //            .edit()
    //            .putStringSet(SSCD_SET, newMetadatas)
    //            .apply();
    //}
//
    //public List<MUSAPKey> listKeys(Context context) {
//
    //}


    public void storeKeyMetaData(Context context, KeyBindReq req) {
        Set<String> metadatas = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                        .getStringSet(SSCD_SET, new HashSet<>());
        Set<String> newMetadatas = new HashSet<>(metadatas);
        newMetadatas.add(req.getSscd());

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putStringSet(SSCD_SET, newMetadatas)
                .apply();
    }

    public Set<String> getMetadata(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getStringSet(SSCD_SET, new HashSet<>());
    }
}
