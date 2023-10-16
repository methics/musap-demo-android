package fi.methics.musap.ui.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.keyuri.MusapKey;
import fi.methics.musap.sdk.sign.MusapSignature;
import fi.methics.musap.sdk.sign.MusapSigner;
import fi.methics.musap.sdk.util.MLog;
import fi.methics.musap.sdk.util.MusapCallback;
import fi.methics.musap.sdk.util.StringUtil;


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

        final String keyuri = args.getString(SignMethodRecyclerViewAdapter.KEY_URI);
        final String dtbsType = args.getString("dtbstype");

        byte[] data;
        if ("jws".equalsIgnoreCase(dtbsType)) {
            JWSObject jws = this.buildSampleJws();
            text.setText(jws.getPayload().toString());
            data = jws.getSigningInput();
        } else {
            final String dtbs   = args.getString(SignMethodRecyclerViewAdapter.DTBS);
            text.setText(dtbs);
            data = StringUtil.toUTF8Bytes(dtbs);
        }

        sign.setOnClickListener(view -> {

            MusapKey key = MusapClient.getKeyByUri(keyuri);
            MusapSigner signer = new MusapSigner(key, this.getActivity());

            try {
                signer.sign(data, new MusapCallback<MusapSignature>() {
                    @Override
                    public void onSuccess(MusapSignature mSig) {
                        String signatureStr = mSig.getB64Signature();
                        MLog.d("Signature successful: " + signatureStr);
//                        Toast.makeText(SigningFragment.this.getContext(), signatureStr, Toast.LENGTH_SHORT).show();
                        sigResult.setText(signatureStr);
                        sign.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onException(MusapException e) {
                        MLog.e("Failed to sign", e.getCause());
                    }
                });
            } catch (MusapException e) {
                MLog.e("Failed to sign", e.getCause());
            }

        });

        return v;
    }

    private JWSObject buildSampleJws() {
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("Sample user")
                .issuer("Sample issuer")
                .build();

        return new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.ES256).keyID("ID123").build(),
                claims.toPayload());
    }
}