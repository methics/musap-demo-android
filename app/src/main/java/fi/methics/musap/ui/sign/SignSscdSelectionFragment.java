package fi.methics.musap.ui.sign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;

import java.nio.charset.StandardCharsets;

import fi.methics.musap.R;
import fi.methics.musap.sdk.sign.SignatureReq;
import fi.methics.musap.sdk.sign.SignatureReqBuilder;
import fi.methics.musap.sdk.util.MBase64;
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

        View v = inflater.inflate(R.layout.fragment_choose_dtbs, container, false);
        Button b = v.findViewById(R.id.button_sign_next);

        b.setOnClickListener(view -> {
            RadioGroup group = v.findViewById(R.id.radio_group_dtbs);
            int pressedId = group.getCheckedRadioButtonId();

            final Bundle args = new Bundle();
            if (pressedId == R.id.radio_sample_jwt) {
                args.putString("dtbstype", "jws");
                // Build the sample jws in SigningFragment
            } else {
                args.putString("dtbstype", "text");
                args.putString("dtbs", "Sample text to sign");
            }

            NavController navController = Navigation.findNavController(SignSscdSelectionFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_sign_to_signMethodFragment, args);
        });

        return v;
    }


}