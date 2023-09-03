package fi.methics.musap.sdk.keyuri;

import java.util.List;

public class MUSAPKeyAttestation {

    private String attestationType;
    private byte[] signature;
    private MUSAPCertificate certificate;
    private List<MUSAPCertificate> certificateChain;
    private String aaguid;

    private MUSAPKeyAttestation(Builder builder) {
        this.attestationType = builder.attestationType;
        this.signature = builder.signature;
        this.certificate = builder.certificate;
        this.certificateChain = builder.certificateChain;
        this.aaguid = builder.aaguid;
    }

    public static class Builder {

        private String attestationType;
        private byte[] signature;
        private MUSAPCertificate certificate;
        private List<MUSAPCertificate> certificateChain;
        private String aaguid;

        public Builder setAttestationType(String attestationType) {
            this.attestationType = attestationType;
            return this;
        }
        public Builder setSignature(byte[] signature) {
            this.signature = signature;
            return this;
        }
        public Builder setCertificate(MUSAPCertificate certificate) {
            this.certificate = certificate;
            return this;
        }
        public Builder setSignature(List<MUSAPCertificate> certificateChain) {
            this.certificateChain = certificateChain;
            return this;
        }
        public Builder setAAGUID(String aaguid) {
            this.aaguid = aaguid;
            return this;
        }
        public MUSAPKeyAttestation build() {
            return new MUSAPKeyAttestation(this);
        }

    }

}
