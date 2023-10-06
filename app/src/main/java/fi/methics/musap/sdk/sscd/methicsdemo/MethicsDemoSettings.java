package fi.methics.musap.sdk.sscd.methicsdemo;

import java.util.HashMap;
import java.util.Map;

import fi.methics.musap.sdk.extension.SscdSettings;

public class MethicsDemoSettings implements SscdSettings {

    private Map<String, String> settings = new HashMap<>();

    @Override
    public Map<String, String> getSettings() {
        return settings;
    }

}
