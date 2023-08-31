package fi.methics.musap.sdk.util;

import android.util.Log;

/**
 * Logging utility method to put all calls to Android logging util to
 * one place
 */
public class MLog {

    private static final String TAG = "MUSAPLog";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }


    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable t) {
        Log.e(TAG, msg, t);
    }

    public static void e(String tag, String msg) {
        Log.d(tag, msg);
    }


}
