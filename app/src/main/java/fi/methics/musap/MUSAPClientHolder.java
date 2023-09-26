package fi.methics.musap;

import fi.methics.musap.sdk.api.MUSAPClient;

/**
 * @deprecated Use static functions in MUSAPClient instead
 */
@Deprecated
public class MUSAPClientHolder {

    private static final MUSAPClient client = new MUSAPClient();


    public static MUSAPClient getClient() {
        return client;
    }
}
