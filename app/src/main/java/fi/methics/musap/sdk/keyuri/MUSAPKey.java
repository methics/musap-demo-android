package fi.methics.musap.sdk.keyuri;

import java.time.Instant;
import java.util.List;

public class MUSAPKey {

    private String keyName;
    private String keyType;
    private String sscdId;
    private String sscdType;
    private Instant createdDate;
    private MUSAPPublicKey publicKey;
    private MUSAPCertificate certificate;
    private List<MUSAPCertificate> certificateChain;
    private List<String> keyUsages;
    private List<MUSAPLoa> loa;

    private MUSAPKey(Builder builder) {
        this.keyName          = builder.keyName;
        this.keyType          = builder.keyType;
        this.sscdId           = builder.sscdId;
        this.sscdType         = builder.sscdType;
        this.createdDate      = builder.createdDate;
        this.publicKey        = builder.publicKey;
        this.certificate      = builder.certificate;
        this.certificateChain = builder.certificateChain;
        this.keyUsages        = builder.keyUsages;
        this.loa              = builder.loa;
        this.createdDate      = Instant.now();
    }

    public String getKeyName() {
        return keyName;
    }

    public String getKeyType() {
        return keyType;
    }

    public String getSscdId() {
        return sscdId;
    }

    public String getSscdType() {
        return sscdType;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public MUSAPPublicKey getPublicKey() {
        return publicKey;
    }

    public MUSAPCertificate getCertificate() {
        return certificate;
    }

    public List<MUSAPCertificate> getCertificateChain() {
        return certificateChain;
    }

    public List<String> getKeyUsages() {
        return keyUsages;
    }

    public List<MUSAPLoa> getLoa() {
        return loa;
    }

    public static class Builder {
        private String keyName;
        private String keyType;
        private String sscdId;
        private String sscdType;
        private Instant createdDate;
        private MUSAPPublicKey publicKey;
        private MUSAPCertificate certificate;
        private List<MUSAPCertificate> certificateChain;
        private List<String> keyUsages;
        private List<MUSAPLoa> loa;

        public Builder setKeyName(String keyName) {
            this.keyName = keyName;
            return this;
        }

        public Builder setKeyType(String keyType) {
            this.keyType = keyType;
            return this;
        }

        public Builder setSscdId(String sscdId) {
            this.sscdId = sscdId;
            return this;
        }

        public Builder setSscdType(String sscdType) {
            this.sscdType = sscdType;
            return this;
        }

        public Builder setCreatedDate(Instant createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder setPublicKey(MUSAPPublicKey publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder setCertificate(MUSAPCertificate certificate) {
            this.certificate = certificate;
            return this;
        }

        public Builder setCertificateChain(List<MUSAPCertificate> certificateChain) {
            this.certificateChain = certificateChain;
            return this;
        }

        public Builder setKeyUsages(List<String> keyUsages) {
            this.keyUsages = keyUsages;
            return this;
        }

        public Builder setLoa(List<MUSAPLoa> loa) {
            this.loa = loa;
            return this;
        }

        public MUSAPKey build() {
            return new MUSAPKey(this);
        }
    }
}
