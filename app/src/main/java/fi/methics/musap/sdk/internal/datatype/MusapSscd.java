package fi.methics.musap.sdk.internal.datatype;

import java.util.List;

public class MusapSscd {

    private String sscdName;
    private String sscdType;
    private String sscdId;
    private String country;
    private String provider;
    private boolean keygenSupported;
    private List<MusapKeyAlgorithm> algorithms;

    private MusapSscd(Builder builder) {
        this.sscdName = builder.sscdName;
        this.sscdType = builder.sscdType;
        this.sscdId   = builder.sscdId;
        this.country  = builder.country;
        this.provider = builder.provider;
        this.keygenSupported = builder.keygenSupported;
        this.algorithms = builder.algorithms;
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

    public List<MusapKeyAlgorithm> getSupportedAlgorithms() {
        return this.algorithms;
    }

    public static class Builder {
        private String sscdName;
        private String sscdType;
        private String sscdId;
        private String country;
        private String provider;
        private boolean keygenSupported;
        private List<MusapKeyAlgorithm> algorithms;

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

        public Builder setSupportedAlgorithms(List<MusapKeyAlgorithm> algorithms) {
            this.algorithms = algorithms;
            return this;
        }

        public MusapSscd build() {
            return new MusapSscd(this);
        }
    }

}
