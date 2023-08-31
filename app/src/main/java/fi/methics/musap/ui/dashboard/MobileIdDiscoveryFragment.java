package fi.methics.musap.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fi.methics.musap.MUSAPClientHolder;
import fi.methics.musap.R;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keydiscovery.KeyBindReqBuilder;

/**
 *
 */
public class MobileIdDiscoveryFragment extends Fragment {


    public MobileIdDiscoveryFragment() {
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
        View v = inflater.inflate(R.layout.fragment_mobile_id_discovery, container, false);

        final TextView phoneView = v.findViewById(R.id.text_discovery_number);
        Button bindButton = v.findViewById(R.id.button_bind_mobileid);
        if (bindButton != null) {
            bindButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = phoneView.getText().toString();

                    KeyBindReq req = new KeyBindReqBuilder()
                            .setMsisdn(number)
                            .setSscd("MobileID")
                                    .createKeyBindReq();

                    MUSAPClientHolder.getClient().bindKey(req);
                }
            });
        }

        return v;
    }
}