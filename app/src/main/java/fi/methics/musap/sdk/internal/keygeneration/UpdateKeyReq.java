package fi.methics.musap.sdk.internal.keygeneration;

import java.util.Map;

import fi.methics.musap.sdk.internal.datatype.MusapKey;

public class UpdateKeyReq {

    /**
     * Target key to update.
     */
    private MusapKey key;

    /**
     * New key alias
     */
    private String alias;

    /**
     * New key DID
     */
    private String did;

    /**
     * New key-specific attributes.
     */
    private Map<String, String> attributes;

    /**
     * New role for the key.
     */
    private String role;

    /**
     * New key state, such as INACTIVE.
     */
    private String state;

    protected UpdateKeyReq(String alias, String did, Map<String, String> attributes, String role, String state) {
        this.alias = alias;
        this.did = did;
        this.attributes = attributes;
        this.role = role;
        this.state = state;
    }

    public MusapKey getKey() {
        return key;
    }

    public String getAlias() {
        return alias;
    }

    public String getDid() {
        return did;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getRole() {
        return role;
    }

    public String getState() {
        return state;
    }
}
