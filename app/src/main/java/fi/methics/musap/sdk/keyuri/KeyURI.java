package fi.methics.musap.sdk.keyuri;


import android.util.Log;

import java.util.Map;
import java.util.Objects;

import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryCriteria;
import fi.methics.musap.sdk.util.LoA;

public class KeyURI {

    private final String name;
    private final String loa;

    public KeyURI(String name, String loa) {
        this.name = name;
        this.loa = loa;
    }

    public String getName() {
        return this.name;
    }

    public boolean matchesCriteria(Map<KeyDiscoveryCriteria, String> criteria) {

        // For every given criteria, check if they match
        for (KeyDiscoveryCriteria c: criteria.keySet()) {
            boolean matches = this.matchesCritiria(c, criteria.get(c));
            // Found nonmatching criteria, return false
            if (!matches) {
                return false;
            }
        }

        // All match
        return true;
    }

    private boolean matchesCritiria(KeyDiscoveryCriteria criteria, String value) {
        Log.d("KeyURI", "Comparing " + criteria + " to value " + value);
        switch (criteria) {
            case LEVEL_OF_ASSURANCE:
                return this.compareLoA(value, this.loa);
            case COUNTRY:
                // TODO.
                break;
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
}
