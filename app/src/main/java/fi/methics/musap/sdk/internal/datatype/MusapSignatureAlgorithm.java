package fi.methics.musap.sdk.internal.datatype;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import java.security.Signature;
import java.util.Objects;

/**
 * Signature algorithm definition class
 */
public class MusapSignatureAlgorithm {

    // Note: Schemes according to SOG-IS ACM
    public static final String SCHEME_RSA_PSS  = "PSS (PKCS#1v2.1)"; // a.k.a. RSA PSS
    public static final String SCHEME_PKCS_1_5 = "PKCS#1v1.5";       // a.k.a. RSA
    public static final String SCHEME_ECDSA    = "EC-DSA";           // a.k.a. ECDSA

    public static final String HASH_SHA256 = "SHA256";
    public static final String HASH_SHA384 = "SHA384";
    public static final String HASH_NONE    = "NONE";

    private String scheme;
    private String hashAlgorithm;
    private MusapKeyAlgorithm keyAlgorithm;

    public MusapSignatureAlgorithm(String scheme, String hashAlgorithm, MusapKey key) {
        this.scheme        = scheme;
        this.hashAlgorithm = hashAlgorithm;
        this.keyAlgorithm  = key.getAlgorithm();
    }

    public MusapSignatureAlgorithm(String scheme, MusapKeyAlgorithm keyAlgorithm) {
        this.scheme        = scheme;
        this.hashAlgorithm = HASH_NONE;
        this.keyAlgorithm  = keyAlgorithm;
    }

    /**
     * Get the algorithm as expected by Java {@link Signature#getInstance(String)}}
     * @return Algorithm
     */
    public String getJavaAlgorithm() {
        String javaHash   = getHashAlgorithm();
        String javaScheme = getJavaScheme();
        return javaHash + "with" + javaScheme;
    }

    /**
     * Get the hash algorithm
     * @return hash algorithm
     */
    public String getHashAlgorithm() {
        if (this.hashAlgorithm == null) return HASH_NONE;
        return this.hashAlgorithm;
    }

    /**
     * Get the scheme
     * @return scheme
     */
    public String getScheme() {
        return this.scheme;
    }

    /**
     * Get scheme as understood by Java Signature
     * @return scheme
     */
    private String getJavaScheme() {
        String javaScheme = scheme;
        switch (scheme) {
            case SCHEME_RSA_PSS:  javaScheme = "RSASSA-PSS"; break;
            case SCHEME_PKCS_1_5: javaScheme = "RSA"; break;
            case SCHEME_ECDSA:    javaScheme = "ECDSA"; break;
        }
        return javaScheme;
    }

    /**
     * Get algorithm as understood by Java signature
     * @return algorithm
     */
    private String getJavaHashAlgorithm() {
        return getHashAlgorithm().replace("-", "").replace("_", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusapSignatureAlgorithm that = (MusapSignatureAlgorithm) o;
        if (!Objects.equals(scheme, that.scheme)) return false;
        return Objects.equals(keyAlgorithm, that.keyAlgorithm);
    }

    @Override
    public int hashCode() {
        int result = scheme != null ? scheme.hashCode() : 0;
        result = 31 * result + (keyAlgorithm != null ? keyAlgorithm.hashCode() : 0);
        return result;
    }
}
