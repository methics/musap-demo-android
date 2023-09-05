package fi.methics.musap.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fi.methics.musap.MUSAPClientHolder;
import fi.methics.musap.R;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keydiscovery.KeyBindReqBuilder;


public class AndroidKeystoreFragment extends Fragment {

    public AndroidKeystoreFragment() {
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
        View v = inflater.inflate(R.layout.fragment_android_keystore, container, false);

        Button b = v.findViewById(R.id.button_bind_android);
        if (b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KeyBindReq req = new KeyBindReqBuilder()
                            .setSscd("Android Keystore")
                            .setGenerateNewKey(true)
                            .createKeyBindReq();

                    MUSAPClientHolder.getClient().bindKey(req);
                }
            });
        }

        return inflater.inflate(R.layout.fragment_android_keystore, container, false);
    }
}