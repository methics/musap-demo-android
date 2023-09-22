package fi.methics.musap.sdk.keyuri;


import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryCriteria;
import fi.methics.musap.sdk.util.LoA;
import fi.methics.musap.sdk.util.MLog;

public class KeyURI {

    public static final String NAME     = "name";
    public static final String LOA      = "loa";
    public static final String COUNTRY  = "country";
    public static final String SSCD     = "sscd";

    private Map<String, String> keyUriMap = new HashMap<>();

    /**
     * Create a new KeyURI with just LoA
     * @param name Name of the key
     * @param loa  LoA of the key
     */
    public KeyURI(String name, String sscd, String loa) {
        if (name != null) keyUriMap.put(NAME, name);
        if (sscd != null) keyUriMap.put(SSCD, sscd);
        if (loa  != null) keyUriMap.put(LOA,  loa);
    }

    /**
     * Create a new KeyURI object from a KeyURI String
     * @param keyURI KeyURI
     */
    public KeyURI(String keyURI) {
        this.keyUriMap = this.parseUri(keyURI);
    }

    /**
     * Parse a KeyURI
     * @param keyURI
     * @return
     */
    private Map<String, String> parseUri(String keyURI) {
        Map<String, String> keyUriMap = new HashMap<>();
        MLog.d("Parsing keyURI " + keyURI);
        if (keyURI == null || !keyURI.contains(",")) {
            return keyUriMap;
        }

        String[] parts = keyURI.replace("mss:", "").split(",");
        if (parts.length == 0) {
            parts = new String[] {keyURI.replace("mss:", "")};
        }

        for (String attribute : parts) {
            if (attribute.contains("=")) {
                String key   = attribute.split("=")[0];
                String value = attribute.split("=")[1];
                MLog.d("Parsed " + key + "=" + value);
                keyUriMap.put(key, value);
            } else {
                MLog.d("Ignoring invalid attribute " + attribute);
            }
        }
        MLog.d("Parsed keyURI to " + keyUriMap);
        return keyUriMap;
    }

    public Map<String, String> getAsMap() {
        return keyUriMap;
    }

    public String getName() {
        return this.keyUriMap.get(NAME);
    }
    public String getLoa() {
        return this.keyUriMap.get(LOA);
    }
    public String getCountry() {
        return this.keyUriMap.get(COUNTRY);
    }

    /**
     * Check if this KeyURI matches given discovery criteria
     * @param criteria List of criteria
     * @return true if match is found
     */
    public boolean matchesCriteria(Map<KeyDiscoveryCriteria, String> criteria) {

        // For every given criteria, check if they match
        for (KeyDiscoveryCriteria c: criteria.keySet()) {
            boolean matches = this.matchesCriteria(c, criteria.get(c));
            // Found nonmatching criteria, return false
            if (!matches) {
                return false;
            }
        }

        // All match
        return true;
    }

    private boolean matchesCriteria(KeyDiscoveryCriteria criteria, String value) {
        Log.d("KeyURI", "Comparing " + criteria + " to value " + value);
        switch (criteria) {
            case LEVEL_OF_ASSURANCE:
                return this.compareLoA(value, this.getLoa());
            case COUNTRY:
                return this.compareValue(value, this.getCountry());
        }

        // Unhandled criteria, return true
        return true;
    }

    private boolean compareLoA(String criteriaValue, String ownValue) {
        return LoA.compareLoA(ownValue, criteriaValue);
    }

    private boolean compareValue(String criteriaValue, String ownValue) {
        return Objects.equals(criteriaValue, ownValue);
    }

    /**
     * Get a String representation of this KeyURI (the actual URI)
     * @return URI
     */
    public String getUri() {
        StringBuilder sb = new StringBuilder("mss:");
        boolean first = true;
        for (String key : this.keyUriMap.keySet()) {
            if (!first) sb.append(",");
            sb.append(key);
            sb.append("=");
            sb.append(this.keyUriMap.get(key));
            first = false;
        }
        return sb.toString();
    }

    /**
     * Check if this KeyURI matches given other KeyURI
     * @param keyUri other KeyURI
     * @return true if match
     */
    public boolean matches(KeyURI keyUri) {
        if (this.equals(keyUri)) return true;
        if (this.getUri().equals(keyUri.getUri())) return true;
        // TODO: Allow some partial match?
        return false;
    }

    @Override
    public String toString() {
        return getUri();
    }
}
