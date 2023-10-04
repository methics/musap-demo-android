package fi.methics.musap.ui.sign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.nio.charset.StandardCharsets;

import fi.methics.musap.R;
import fi.methics.musap.sdk.sign.SignatureReq;
import fi.methics.musap.sdk.sign.SignatureReqBuilder;
import fi.methics.musap.sdk.yubikey.YubiKeyExtension;

public class SignSscdSelectionFragment extends Fragment {


    public SignSscdSelectionFragment() {
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

        View v = inflater.inflate(R.layout.fragment_sign, container, false);

        Button b = v.findViewById(R.id.button_sign_next);

        b.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(SignSscdSelectionFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
            Bundle args = new Bundle();
            navController.navigate(R.id.action_navigation_sign_to_signMethodFragment, args);
        });

        Button yubiSign = v.findViewById(R.id.button_yubi_sign);
        yubiSign.setOnClickListener(view -> {
            SignatureReq req = new SignatureReqBuilder()
                    .setData("Sample text".getBytes(StandardCharsets.UTF_8))
                    .setActivity(SignSscdSelectionFragment.this.getActivity())
                    .createSignatureReq();
            YubiKeyExtension extension = new YubiKeyExtension(SignSscdSelectionFragment.this.getActivity());
            extension.signAsync(req);
        });

        return v;
    }
}