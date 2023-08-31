package fi.methics.musap.sdk.keyuri;

public class MUSAPSscd {

    private String sscdName;
    private String sscdType;
    private String sscdId;
    private String country;
    private String provider;

    private boolean keygenSupported;

    private MUSAPSscd(String sscdName, String sscdType, String sscdId, String country, String provider, boolean keygenSupported) {
        this.sscdName = sscdName;
        this.sscdType = sscdType;
        this.sscdId = sscdId;
        this.country = country;
        this.provider = provider;
        this.keygenSupported = keygenSupported;
    }

    public String getSscdName() {
        return sscdName;
    }

    public String getSscdType() {
        return sscdType;
    }

    public String getSscdId() {
        return sscdId;
    }

    public String getCountry() {
        return country;
    }

    public String getProvider() {
        return provider;
    }

    public boolean isKeygenSupported() {
        return keygenSupported;
    }

    public static class Builder {
        private String sscdName;
        private String sscdType;
        private String sscdId;
        private String country;
        private String provider;
        private boolean keygenSupported;

        public Builder setSscdName(String sscdName) {
            this.sscdName = sscdName;
            return this;
        }

        public Builder setSscdType(String sscdType) {
            this.sscdType = sscdType;
            return this;
        }

        public Builder setSscdId(String sscdId) {
            this.sscdId = sscdId;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder setKeygenSupported(boolean keygenSupported) {
            this.keygenSupported = keygenSupported;
            return this;
        }

        public MUSAPSscd build() {
            return new MUSAPSscd(sscdName, sscdType, sscdId, country, provider, keygenSupported);
        }
    }

}
