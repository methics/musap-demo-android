package fi.methics.musap.sdk.sscd.rest204;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import fi.methics.musap.sdk.extension.SscdSettings;
import fi.methics.musap.sdk.internal.util.MBase64;

public class Rest204Settings implements SscdSettings {

    public static final String SETTINGS_REST_URL   = "resturl";
    public static final String SETTINGS_RAW_FORMAT = "format_raw";
    public static final String SETTINGS_CMS_FORMAT = "format_cms";
    public static final String SETTINGS_TIMEOUT    = "timeout";
    public static final String SETTINGS_COUNTRY    = "country";
    public static final String SETTINGS_PROVIDER   = "provider";
    public static final String SETTINGS_SSCD_NAME  = "sscdname";

    // AP settings
    public static final String SETTINGS_REST_APID    = "apid";
    public static final String SETTINGS_REST_USERID  = "userid";
    public static final String SETTINGS_REST_API_KEY = "apikey";

    private Map<String, String> settings = new HashMap<>();
    private Duration timeout;

    /**
     * Construct REST SSCD settings
     * @param restUrl   REST end-point URL
     * @param cmsFormat CMS format String
     * @param rawFormat RAW format String (PKCS#1)
     */
    public Rest204Settings(String restUrl, String cmsFormat, String rawFormat, Duration timeout) {
        settings.put(SETTINGS_REST_URL,   restUrl);
        settings.put(SETTINGS_RAW_FORMAT, cmsFormat);
        settings.put(SETTINGS_CMS_FORMAT, rawFormat);
        settings.put(SETTINGS_TIMEOUT, String.valueOf(timeout.toMillis()));
        this.timeout = timeout;
    }

    public void setApId(String apid) {
        settings.put(SETTINGS_REST_APID, apid);
        settings.put(SETTINGS_REST_USERID, MBase64.toBase64(apid));
    }

    public void setApiKey(String apikey) {
        settings.put(SETTINGS_REST_API_KEY, apikey);
    }

    @Override
    public Map<String, String> getSettings() {
        return settings;
    }

    public String getRestUrl() {
        return this.getSetting(SETTINGS_REST_URL);
    }

    public String getRestUserId() {
        return this.getSetting(SETTINGS_REST_USERID);
    }

    public String getRestApiKey() {
        return this.getSetting(SETTINGS_REST_API_KEY);
    }
    public String getRawFormatUri() {
        return this.getSetting(SETTINGS_RAW_FORMAT);
    }

    public String getCmsFormatUri() {
        return this.getSetting(SETTINGS_CMS_FORMAT);
    }

    public String getSscdName() {
        String name = this.getSetting(SETTINGS_SSCD_NAME);
        return name != null ? name : "REST 204";
    }

    public String getProvider() {
        String provider = this.getSetting(SETTINGS_PROVIDER);
        return provider != null ? provider : "Methics";
    }
    public String getCountry() {
        String country = this.getSetting(SETTINGS_COUNTRY);
        return country != null ? country : "FI";
    }

    public Duration getTimeout() {
        if (this.timeout == null) return Duration.ofMinutes(2);
        return this.timeout;
    }

}
