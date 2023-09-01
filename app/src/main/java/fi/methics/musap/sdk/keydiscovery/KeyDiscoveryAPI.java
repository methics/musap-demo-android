package fi.methics.musap.sdk.keydiscovery;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;

public class KeyDiscoveryAPI {

    private Context context;

    public KeyDiscoveryAPI(Context context) {
        this.context = context;
    }

    @Deprecated // Use listKeys() instead
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

    /**
     * List all SSCDs integrated to this MUSAP Library
     * @return SSCD list
     */
    public List<MUSAPSscd> listSscds() {
        return new ArrayList<>();
    }

    /**
     * Find MUSAP SSCDs that match given search criteria
     * @param req SSCD search request
     * @return MUSAP SSCDs
     */
    public List<MUSAPSscd> listMatchingSscds(SscdSearchReq req) {
        return new ArrayList<>();
    }

    /**
     * Find MUSAP keys that match given search criteria
     * @param req Key search request
     * @return Matching MUSAP keys
     */
    public List<MUSAPKey> findKey(KeySearchReq req) {

        List<MUSAPKey> keys = this.listKeys();
        List<MUSAPKey> result = new ArrayList<>();
        for (MUSAPKey key : keys) {
            if (req.matches(key)) {
                result.add(key);
            }
        }
        return result;
    }

    /**
     * List available MUSAP keys
     * @return List of available keys
     */
    public List<MUSAPKey> listKeys() {
        return new KeyMetaDataStorage(context).listKeys();
    }

}
