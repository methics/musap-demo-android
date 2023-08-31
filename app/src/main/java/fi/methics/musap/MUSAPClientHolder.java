package fi.methics.musap;

import fi.methics.musap.sdk.api.MUSAPClient;

public class MUSAPClientHolder {

    private static final MUSAPClient client = new MUSAPClient();


    public static MUSAPClient getClient() {
        return client;
    }
}
