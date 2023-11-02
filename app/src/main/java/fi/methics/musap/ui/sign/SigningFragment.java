package fi.methics.musap.ui.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.datatype.KeyAlgorithm;
import fi.methics.musap.sdk.internal.datatype.MusapSignature;
import fi.methics.musap.sdk.internal.datatype.SignatureAlgorithm;
import fi.methics.musap.sdk.internal.sign.MusapSigner;
import fi.methics.musap.sdk.internal.sign.SignatureReq;
import fi.methics.musap.sdk.internal.util.MLog;
import fi.methics.musap.sdk.api.MusapCallback;
import fi.methics.musap.sdk.internal.util.StringUtil;


public class SigningFragment extends Fragment {

    public SigningFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_sign, container, false);

        Button sign = v.findViewById(R.id.button_aks_sign);
        TextView text = v.findViewById(R.id.text_aks_dtbs);
        TextView sigResult = v.findViewById(R.id.text_signature_result);
        final Bundle args = getArguments();

        final String keyuri   = args.getString(SignMethodRecyclerViewAdapter.KEY_URI);
        final String dtbsType = args.getString("dtbstype");

        MusapKey key = MusapClient.getKeyByUri(keyuri);

        KeyAlgorithm keyAlgo = key.getAlgorithm();
        if (keyAlgo == null) {
            keyAlgo = KeyAlgorithm.ECC_P256_R1;
        }
        SignatureAlgorithm algorithm = keyAlgo.isEc() ? SignatureAlgorithm.SHA256_WITH_ECDSA :
                                                        SignatureAlgorithm.SHA256_WITH_RSA;

        final JWSObject jws;
        byte[] data;
        if ("jws".equalsIgnoreCase(dtbsType)) {
            jws = this.buildSampleJws(algorithm);
            text.setText(jws.getPayload().toString());
            data = jws.getSigningInput();
        } else {
            jws = null;
            final String dtbs = args.getString(SignMethodRecyclerViewAdapter.DTBS);
            text.setText(dtbs);
            data = StringUtil.toUTF8Bytes(dtbs);
        }

        final SignatureReq req = new SignatureReq.Builder(algorithm)
                .setKey(key)
                .setData(data)
                .createSignatureReq();

        sign.setOnClickListener(view -> {

            MusapSigner signer = new MusapSigner(key, this.getActivity());
            try {
                signer.sign(req, new MusapCallback<MusapSignature>() {
                    @Override
                    public void onSuccess(MusapSignature mSig) {

                        String signatureStr;
                        if ("jws".equalsIgnoreCase(dtbsType)) {
                            signatureStr = attachSignature(jws, mSig).serialize();
                            MLog.d("Public key: " + mSig.getKey().getPublicKey().getPEM());
                        } else {
                            signatureStr = mSig.getB64Signature();
                        }
                        MLog.d("Signature successful: " + signatureStr);
                        sigResult.setText(signatureStr);
                        sign.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onException(MusapException e) {
                        MLog.e("Failed to sign", e.getCause());
                        Toast.makeText(getContext(), "Failed to sign: " + e.getMessage() + "(" + e.getErrorName() + ")", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MusapException e) {
                MLog.e("Failed to sign", e.getCause());
            }
        });

        return v;
    }

    /**
     * Attach signature to the JWSObject
     * @param orig Original JWSObject
     * @param sig  Signature
     * @return JWSObject with signature attached
     */
    private JWSObject attachSignature(JWSObject orig, MusapSignature sig) {
        try {
            Base64URL header    = orig.getHeader().toBase64URL();
            Base64URL payload   = orig.getPayload().toBase64URL();
            Base64URL signature = Base64URL.encode(sig.getRawSignature());
            return new JWSObject(header, payload, signature);
        } catch (Exception e) {
            MLog.e("Failed to parse JWS", e);
            return orig;
        }
    }

    /**
     * Build a sample JWS using the given algorithm
     * @param algorithm Algorithm
     * @return Unsigned JWS
     */
    private JWSObject buildSampleJws(SignatureAlgorithm algorithm) {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("Sample user")
                .issuer("Sample issuer")
                .build();

        JWSHeader.Builder builder = new JWSHeader.Builder(JWSAlgorithm.parse(algorithm.getJwsAlgorithm()));
        builder.keyID("ID123");

        final JWSHeader header = builder.build();
        final Payload  payload = claims.toPayload();
        return new JWSObject(header, payload);
    }
}