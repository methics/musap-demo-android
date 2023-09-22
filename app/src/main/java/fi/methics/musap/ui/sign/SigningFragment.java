package fi.methics.musap.ui.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fi.methics.musap.MUSAPClientHolder;
import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MBase64;
import fi.methics.musap.sdk.api.MUSAPException;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
import fi.methics.musap.sdk.sign.MUSAPSigner;
import fi.methics.musap.sdk.util.MLog;


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
        final View v = inflater.inflate(R.layout.fragment_android_keystore_sign, container, false);

        Button sign = v.findViewById(R.id.button_aks_sign);
        final Bundle args = getArguments();

        final String dtbs   = args.getString(SignMethodRecyclerViewAdapter.DTBS);
        final String keyuri = args.getString(SignMethodRecyclerViewAdapter.KEY_URI);

        TextView text = v.findViewById(R.id.text_aks_dtbs);
        text.setText(dtbs);

        sign.setOnClickListener(view -> {
            String dtbs1 = v.findViewById(R.id.text_aks_dtbs).toString();
            byte[] data = MBase64.toBytes(dtbs1);

            MUSAPKey key = MUSAPClientHolder.getClient().getKeyByUri(keyuri);
            MUSAPSigner signer = new MUSAPSigner(key);

            try {
                byte[] signature = signer.sign(data).getRawSignature();
                String signatureStr = MBase64.toBase64String(signature);
                Toast.makeText(SigningFragment.this.getContext(), signatureStr, Toast.LENGTH_SHORT).show();

            } catch (MUSAPException e) {
                MLog.e("Failed to sign", e.getCause());
            }

        });

        return v;
    }
}