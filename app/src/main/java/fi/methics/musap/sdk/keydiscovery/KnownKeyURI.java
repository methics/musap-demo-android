package fi.methics.musap.sdk.keydiscovery;

import java.util.ArrayList;
import java.util.List;

import fi.methics.musap.sdk.keyuri.KeyURI;

/**
 * Container for all known KeyURIs.
 * The actual implementation of this is still open.
 */
public class KnownKeyURI {

    public List<KeyURI> getAllKnownKeyURIs() {
        List<KeyURI> uris = new ArrayList<>();
        KeyURI mobileId = new KeyURI("MobileID", "high");
        KeyURI remoteSign = new KeyURI("RemoteSign", "substantial");
        KeyURI localStore = new KeyURI("Android Keystore", "low");

        uris.add(mobileId);
        uris.add(remoteSign);
        uris.add(localStore);

        return uris;
    }

}
