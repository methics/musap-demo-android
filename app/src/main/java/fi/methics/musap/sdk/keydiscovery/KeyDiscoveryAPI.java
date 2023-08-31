package fi.methics.musap.sdk.keydiscovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;

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

    public List<MUSAPSscd> listSscds() {
        return new ArrayList<>();
    }

    public List<MUSAPSscd> listMatchingSscds(SscdSearchReq req) {
        return new ArrayList<>();
    }

    public List<MUSAPKey> findKey(KeySearchReq req) {
        return new ArrayList<>();
    }

    public List<MUSAPKey> listKeys() {
        return new ArrayList<>();
    }

}
