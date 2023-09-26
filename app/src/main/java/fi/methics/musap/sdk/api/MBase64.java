package fi.methics.musap.sdk.api;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Base64 util for MUSAP use.
 */
public class MBase64 {

    private static final int FLAGS = Base64.NO_WRAP;

    /**
     * Convert a string to a base 64 string.
     * @param s
     * @return
     */
    public static String toBase64(String s) {
        if (s == null) {
            return null;
        }
        return Base64.encodeToString(s.getBytes(StandardCharsets.UTF_8), FLAGS);
    }

    /**
     * Convert a string into a byte array.
     * @param s
     * @return
     */
    public static byte[] toBytes(String s) {
        if (s == null) {
            return null;
        }

        return s.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Convert a byte array into base64 format.
     * @param b
     * @return
     */
    public static String toBase64String(byte[] b) {
        return Base64.encodeToString(b, FLAGS);
    }
}
