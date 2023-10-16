package fi.methics.musap.sdk.internal.datatype;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.internal.util.MLog;

public class MusapKey {

    private String keyName;
    private String keyType;
    private String sscdId;
    private String sscdType;
    private Instant createdDate;
    private MusapPublicKey publicKey;
    private MusapCertificate certificate;
    private List<MusapCertificate> certificateChain;
    private List<MusapKeyAttribute> attributes;
    private List<String> keyUsages;
    private List<MusapLoA> loa;
    private String keyAlgorithm;
    private String keyUri;

    private MusapKeyAttestation attestation;

    private MusapKey(Builder builder) {
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
        this.attributes       = builder.attributes;
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

    public MusapPublicKey getPublicKey() {
        return publicKey;
    }

    public MusapCertificate getCertificate() {
        return certificate;
    }

    public List<MusapCertificate> getCertificateChain() {
        return certificateChain;
    }

    public List<String> getKeyUsages() {
        return keyUsages;
    }

    public List<MusapLoA> getLoa() {
        return loa;
    }

    public String getKeyAlgorithm() {
        return this.keyAlgorithm;
    }

    public KeyURI getKeyUri() {
        return new KeyURI(this.keyUri);
    }

    public List<MusapKeyAttribute> getAttributes() {
        return this.attributes;
    }

    public MusapKeyAttribute getAttribute(String name) {
        if (name == null) return null;
        return this.attributes.stream().filter(n -> name.equals(n.name)).findFirst().orElse(null);
    }

    public String getAttributeValue(String name) {
        MusapKeyAttribute attr = this.getAttribute(name);
        if (attr == null) return null;
        return attr.value;
    }

    /**
     * Get a handle to the SSCD that created this MUSAP key
     * @return SSCD
     */
    public MusapSscdInterface getSscd() {
        if (this.sscdId == null) {
            MLog.d("No sscdid found");
            return null;
        }
        MLog.d("Looking for an SSCD with id " + this.sscdId);
        for (MusapSscdInterface sscd : MusapClient.listEnabledSSCDS()) {
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
        private MusapPublicKey publicKey;
        private MusapCertificate certificate;
        private List<MusapCertificate> certificateChain;
        private List<MusapKeyAttribute> attributes = new ArrayList<>();
        private List<String> keyUsages;
        private List<MusapLoA> loa;
        private String keyAlgorithm;
        private String keyUri;

        private MusapKeyAttestation attestation;

        public Builder setKeyName(String keyName) {
            this.keyName = keyName;
            return this;
        }

        public Builder setKeyType(String keyType) {
            this.keyType = keyType;
            return this;
        }

        public Builder setKeyAttribute(String name, String value) {
            this.attributes.add(new MusapKeyAttribute(name, value));
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

        public Builder setPublicKey(MusapPublicKey publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder setCertificate(MusapCertificate certificate) {
            this.certificate = certificate;
            if (this.certificate != null && this.publicKey == null) {
                this.publicKey = this.certificate.getPublicKey();
            }
            return this;
        }

        public Builder setCertificateChain(List<MusapCertificate> certificateChain) {
            this.certificateChain = certificateChain;
            return this;
        }

        public Builder setKeyUsages(List<String> keyUsages) {
            this.keyUsages = keyUsages;
            return this;
        }

        public Builder setLoa(List<MusapLoA> loa) {
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

        public Builder setAttestation(MusapKeyAttestation attestation) {
            this.attestation = attestation;
            return this;
        }

        public MusapKey build() {
            return new MusapKey(this);
        }
    }
}
