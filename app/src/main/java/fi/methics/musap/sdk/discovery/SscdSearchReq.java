package fi.methics.musap.sdk.discovery;

import fi.methics.musap.sdk.keyuri.MUSAPSscd;

public class SscdSearchReq {

    private String sscdType;
    private String country;
    private String provider;
    private String keyAlgorithm;

    public SscdSearchReq(Builder builder) {
        this.sscdType     = builder.sscdType;
        this.country      = builder.country;
        this.provider     = builder.provider;
        this.keyAlgorithm = builder.keyAlgorithm;
    }

    public String getSscdType() {
        return sscdType;
    }

    public String getCountry() {
        return country;
    }

    public String getProvider() {
        return provider;
    }

    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public boolean matches(MUSAPSscd sscd) {
        if (this.keyAlgorithm != null && !sscd.getSupportedKeyAlgorithms().contains(this.keyAlgorithm)) return false;
        return true;
    }

    public class Builder {
        private String sscdType;
        private String country;
        private String provider;
        private String keyAlgorithm;

        public Builder setSscdType(String sscdType) {
            this.sscdType = sscdType;
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

        public Builder setKeyAlgorithm(String keyAlgorithm) {
            this.keyAlgorithm = keyAlgorithm;
            return this;
        }

        public SscdSearchReq build() {
            return new SscdSearchReq(this);
        }
    }

}
