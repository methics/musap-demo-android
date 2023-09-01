package fi.methics.musap.sdk.api;

import fi.methics.musap.sdk.MUSAPSscdType;

public class MUSAPSscd {

    private MUSAPSscdType sscdType;

    public MUSAPSscd(MUSAPSscdType sscdType) {
        this.sscdType = sscdType;
    }


    public MUSAPSscdType getSscdType() {
        return sscdType;
    }
}
