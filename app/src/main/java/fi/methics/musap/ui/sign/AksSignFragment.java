package fi.methics.musap.ui.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fi.methics.musap.R;
import fi.methics.musap.sdk.util.StringUtil;

@Deprecated
public class AksSignFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AksSignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_sign, container, false);

        Button sign = v.findViewById(R.id.button_aks_sign);
        final Bundle args = getArguments();

        final String dtbs = args.getString(SignMethodRecyclerViewAdapter.DTBS);
        final String alias = args.getString(SignMethodRecyclerViewAdapter.KEY_ALIAS);

        TextView text = v.findViewById(R.id.text_aks_dtbs);
        text.setText(dtbs);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dtbs = v.findViewById(R.id.text_aks_dtbs).toString();
                byte[] data = StringUtil.toUTF8Bytes(dtbs);

                //MUSAPKey key = MUSAPClientHolder.getClient().getKeyByName(alias);
                //MUSAPSigner signer = new MUSAPSigner(key);
//
                //try {
                //    byte[] signature = signer.sign(data);
                //    String signatureStr = MBase64.toBase64String(signature);
                //    Toast.makeText(AksSignFragment.this.getContext(), signatureStr, Toast.LENGTH_SHORT).show();
//
                //} catch (MUSAPException e) {
                //    MLog.e("Failed to sign", e.getCause());
                //}

            }
        });

        return v;
    }
}