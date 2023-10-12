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
import com.nimbusds.jwt.JWTClaimsSet;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.sign.MUSAPSignature;
import fi.methics.musap.sdk.sign.MUSAPSigner;
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

            MUSAPKey key = MUSAPClient.getKeyByUri(keyuri);
            MUSAPSigner signer = new MUSAPSigner(key, this.getActivity());

            try {
                signer.sign(data, new MusapCallback<MUSAPSignature>() {
                    @Override
                    public void onSuccess(MUSAPSignature mSig) {
                        String signatureStr = mSig.getB64Signature();
                        MLog.d("Signature successful: " + signatureStr);
                        Toast.makeText(SigningFragment.this.getContext(), signatureStr, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onException(MUSAPException e) {
                        MLog.e("Failed to sign", e.getCause());
                    }
                });
            } catch (MUSAPException e) {
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