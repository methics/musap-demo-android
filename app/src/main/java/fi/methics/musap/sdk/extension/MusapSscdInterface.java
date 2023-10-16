package fi.methics.musap.sdk.extension;

import fi.methics.musap.sdk.discovery.KeyBindReq;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keyuri.MusapKey;
import fi.methics.musap.sdk.keyuri.MusapSscd;
import fi.methics.musap.sdk.sign.MusapSignature;
import fi.methics.musap.sdk.sign.SignatureReq;

/**
 * MUSAP SSCD extension interface. Every SSCD should implement this.
 * TODO: The API here should be changed to be asynchronous with some sort of callbacks
 */
public interface MusapSscdInterface<T extends SscdSettings> {

    // TODO: Should the bindKey and generateKey be the same API?
    //       Each SSCD basically supports either binding or generating.
    //       Usually not both at the same time.

    /**
     * Bind an existing key to this MUSAP library
     * @param req Key bind request
     * @throws Exception TODO: Make new MUSAPException class?
     * @return
     */
    public MusapKey bindKey(KeyBindReq req) throws Exception;

    /**
     * Generate a new key with this SSCD. Note that this SSCD must support
     * @param req Key generation request
     * @throws Exception TODO: Make new MUSAPException class?
     * @return Recently generated MUSAPKey
     */
    public MusapKey generateKey(KeyGenReq req) throws Exception;

    /**
     * Sign with the SSCD
     * @param req Signature request
     * @return Signature response
     * @throws Exception
     */
    public MusapSignature sign(SignatureReq req) throws Exception;

    /**
     * Get SSCD info. Must not return null.
     * @return SSCD info
     */
    public MusapSscd getSscdInfo();

    /**
     * Does this SSCD support key generation?
     * @return true if key generation is supported
     */
    public default boolean isKeygenSupported() {
        return this.getSscdInfo().isKeygenSupported();
    }

    public T getSettings();

}
