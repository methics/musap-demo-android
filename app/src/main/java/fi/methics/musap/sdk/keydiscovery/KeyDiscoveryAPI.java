package fi.methics.musap.sdk.keydiscovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.methics.musap.sdk.keyuri.KeyURI;

public class KeyDiscoveryAPI {

    public List<KeyURI> listMatchingMethods(Map<KeyDiscoveryCriteria, String> criteria) {
        List<KeyURI> matching = new ArrayList<>();

        List<KeyURI> knownUris = new KnownKeyURI().getAllKnownKeyURIs();

        for (KeyURI keyUri: knownUris) {
            if (keyUri.matchesCriteria(criteria)) {
                matching.add(keyUri);
            }
        }

        return matching;
    }

}
