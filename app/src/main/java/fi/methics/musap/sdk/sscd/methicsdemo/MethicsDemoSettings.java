package fi.methics.musap.sdk.sscd.methicsdemo;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import fi.methics.musap.sdk.extension.SscdSettings;

public class MethicsDemoSettings implements SscdSettings {

    public static final String SETTINGS_DEMO_URL   = "demourl";
    public static final String SETTINGS_RAW_FORMAT = "format_raw";
    public static final String SETTINGS_CMS_FORMAT = "format_cms";
    public static final String SETTINGS_TIMEOUT    = "timeout";

    private Map<String, String> settings = new HashMap<>();
    private Duration timeout;

    /**
     * Construct Methics Demo SSCD settings
     * @param demoUrl   Demo end-point URL
     * @param cmsFormat CMS format String
     * @param rawFormat RAW format String (PKCS#1)
     */
    public MethicsDemoSettings(String demoUrl, String cmsFormat, String rawFormat, Duration timeout) {
        settings.put(SETTINGS_DEMO_URL,   demoUrl);
        settings.put(SETTINGS_RAW_FORMAT, cmsFormat);
        settings.put(SETTINGS_CMS_FORMAT, rawFormat);
        settings.put(SETTINGS_TIMEOUT, String.valueOf(timeout.toMillis()));
        this.timeout = timeout;
    }

    @Override
    public Map<String, String> getSettings() {
        return settings;
    }

    public String getDemoUrl() {
        return this.getSetting(SETTINGS_DEMO_URL);
    }

    public String getRawFormatUri() {
        return this.getSetting(SETTINGS_RAW_FORMAT);
    }

    public String getCmsFormatUri() {
        return this.getSetting(SETTINGS_CMS_FORMAT);
    }

    public Duration getTimeout() {
        if (this.timeout == null) return Duration.ofMinutes(2);
        return this.timeout;
    }

}
