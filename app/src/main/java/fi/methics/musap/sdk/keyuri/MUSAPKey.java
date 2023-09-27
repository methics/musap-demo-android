package fi.methics.musap.sdk.keyuri;

import java.time.Instant;
import java.util.List;

import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.util.MLog;

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
    private String keyAlgorithm;
    private String keyUri;

    private MUSAPKeyAttestation attestation;

    private MUSAPKey(Builder builder) {
        this.keyName          = builder.keyName;
        this.keyType          = builder.keyType;
        this.sscdId           = builder.sscdId;
        this.sscdType         = builder.sscdType;
        this.publicKey        = builder.publicKey;
        this.certificate      = builder.certificate;
        this.certificateChain = builder.certificateChain;
        this.keyUsages        = builder.keyUsages;
        this.loa              = builder.loa;
        this.keyAlgorithm     = builder.keyAlgorithm;
        this.keyUri           = builder.keyUri;
        this.attestation      = builder.attestation;
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

    public String getKeyAlgorithm() {
        return this.keyAlgorithm;
    }

    public KeyURI getKeyUri() {
        return new KeyURI(this.keyUri);
    }

    /**
     * Get a handle to the SSCD that created this MUSAP key
     * @return SSCD
     */
    public MUSAPSscdInterface getSscd() {
        if (this.sscdId == null) {
            MLog.d("No sscdid found");
            return null;
        }
        MLog.d("Looking for an SSCD with id " + this.sscdId);
        for (MUSAPSscdInterface sscd : MUSAPClient.listEnabledSSCDS()) {
            if (this.sscdId.equals(sscd.getSscdInfo().getSscdId())) {
                MLog.d("Found SSCD with id " + this.sscdId);
                return sscd;
            }
        }
        return null;
    }

    public static class Builder {
        private String keyName;
        private String keyType;
        private String sscdId;
        private String sscdType;
        private MUSAPPublicKey publicKey;
        private MUSAPCertificate certificate;
        private List<MUSAPCertificate> certificateChain;
        private List<String> keyUsages;
        private List<MUSAPLoa> loa;
        private String keyAlgorithm;
        private String keyUri;

        private MUSAPKeyAttestation attestation;

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

        public Builder setKeyAlgorithm(String keyAlgorithm) {
            this.keyAlgorithm = keyAlgorithm;
            return this;
        }

        public Builder setKeyUri(String keyUri) {
            this.keyUri = keyUri;
            return this;
        }

        public Builder setAttestation(MUSAPKeyAttestation attestation) {
            this.attestation = attestation;
            return this;
        }

        public MUSAPKey build() {
            return new MUSAPKey(this);
        }
    }
}
