package fi.methics.musap.sdk.discovery;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.keyuri.MUSAPSscd;

public class KeyDiscoveryAPI {

    private Context context;
    private static List<MUSAPSscdInterface> sscds = new ArrayList<>();
    private MetadataStorage storage;

    public KeyDiscoveryAPI(Context context) {
        this.context = context;
        this.storage = new MetadataStorage(this.context);
    }

    /**
     * List all SSCDs integrated to this MUSAP Library
     * @return SSCD list
     */
    public List<MUSAPSscdInterface> listSscds() {
        return sscds;
    }

    /**
     * Find MUSAP SSCDs that match given search criteria
     * @param req SSCD search request
     * @return MUSAP SSCDs
     */
    public List<MUSAPSscdInterface> listMatchingSscds(SscdSearchReq req) {
        return sscds;
    }

    /**
     * List active SSCDs. This returns all SSCDs that have either a generated or a bound key.
     * @return Active SSCDs
     */
    public List<MUSAPSscd> listActiveSSCDs() {
        return storage.listSscds();
    }

    /**
     * Enable given SSCD. The SSCD will be available for the user to select after this call.
     * Get the list of available SSCDs with {@link #listSscds()}.
     * @param sscd SSCD
     */
    public void enableSSCD(MUSAPSscdInterface sscd) {
        sscds.add(sscd);
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
        return new MetadataStorage(context).listKeys();
    }

}
