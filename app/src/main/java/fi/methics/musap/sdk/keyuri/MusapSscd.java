package fi.methics.musap.sdk.keyuri;

import java.util.List;

public class MusapSscd {

    private String sscdName;
    private String sscdType;
    private String sscdId;
    private String country;
    private String provider;
    private boolean keygenSupported;
    private List<String> supportedKeyAlgorithms;

    private MusapSscd(Builder builder) {
        this.sscdName = builder.sscdName;
        this.sscdType = builder.sscdType;
        this.sscdId   = builder.sscdId;
        this.country  = builder.country;
        this.provider = builder.provider;
        this.keygenSupported = builder.keygenSupported;
        this.supportedKeyAlgorithms = builder.supportedKeyAlgorithms;
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

    public List<String> getSupportedKeyAlgorithms() {
        return this.supportedKeyAlgorithms;
    }

    public static class Builder {
        private String sscdName;
        private String sscdType;
        private String sscdId;
        private String country;
        private String provider;
        private boolean keygenSupported;
        private List<String> supportedKeyAlgorithms;

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

        public Builder setSupportedKeyAlgorithms(List<String> supportedKeyAlgorithms) {
            this.supportedKeyAlgorithms = supportedKeyAlgorithms;
            return this;
        }

        public MusapSscd build() {
            return new MusapSscd(this);
        }
    }

}
