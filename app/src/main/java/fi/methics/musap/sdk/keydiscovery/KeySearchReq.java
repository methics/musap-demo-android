package fi.methics.musap.sdk.keydiscovery;

import fi.methics.musap.sdk.keyuri.KeyURI;
import fi.methics.musap.sdk.keyuri.MUSAPKey;

public class KeySearchReq {

    private String sscdType;
    private String country;
    private String provider;
    private String keyAlgorithm;
    private String keyUri;

    public KeySearchReq(Builder builder) {
        this.sscdType     = builder.sscdType;
        this.country      = builder.country;
        this.provider     = builder.provider;
        this.keyAlgorithm = builder.keyAlgorithm;
        this.keyUri       = builder.keyUri;
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

    public boolean matches(MUSAPKey key) {
        if (this.keyAlgorithm != null && !this.keyAlgorithm.equals(key.getKeyAlgorithm())) return false;
        if (this.keyUri       != null && !new KeyURI(this.keyUri).matches(key.getKeyUri())) return false;
        return true;
    }

    public class Builder {
        private String sscdType;
        private String country;
        private String provider;
        private String keyAlgorithm;
        private String keyUri;

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

        public Builder setKeyUri(String keyUri) {
            this.keyUri = keyUri;
            return this;
        }

        public KeySearchReq build() {
            return new KeySearchReq(this);
        }
    }

}
