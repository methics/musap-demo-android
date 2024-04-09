package fi.methics.musap.ui;

import android.content.Context;
import android.content.SharedPreferences;

public class AppStorage {

    private static final String PREF_NAME =  "musapdemo";

    public static void storeMsisdn(Context context, String msisdn) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString("msisdn", msisdn).apply();
    }

    public static String getMsisdn(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("msisdn", null);
    }
}
