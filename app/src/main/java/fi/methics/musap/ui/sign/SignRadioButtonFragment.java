package fi.methics.musap.ui.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fi.methics.musap.R;
import fi.methics.musap.databinding.FragmentSignRadioButtonBinding;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.internal.datatype.KeyURI;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.util.MLog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignRadioButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignRadioButtonFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    protected static final String KEY_URI   = "keyuri";
    protected static final String DTBS      = "dtbs";

    // Map radio button ID to KeyURI string
    private Map<String, String> displayNameToUri;

    private FragmentSignRadioButtonBinding binding;


    public SignRadioButtonFragment() {
        // Required empty public constructor
    }


    public static SignRadioButtonFragment newInstance(List<String> displayNames, List<String> keyUris) {
        SignRadioButtonFragment fragment = new SignRadioButtonFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, new ArrayList<>(displayNames));
        args.putStringArrayList(ARG_PARAM2, new ArrayList<>(keyUris));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.binding = FragmentSignRadioButtonBinding.inflate(inflater);

        List<KeyURI> keys = MusapClient.listKeys().stream().map(MusapKey::getKeyUri).collect(Collectors.toList());
        Map<String, String> displayNameToUri = new HashMap<>();
        MLog.d("Found keys: ");
        for (KeyURI keyuri : keys) {
            MLog.d("  " + keyuri);
            displayNameToUri.put(this.makeDisplayName(keyuri), keyuri.getUri());
        }

        this.displayNameToUri = displayNameToUri;
        this.setOnClickListener();
        this.makeRadioButtons();

        return this.binding.getRoot();
    }

    private void setOnClickListener() {
        binding.buttonSign.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString(KEY_URI, this.getSelectedKeyUri());
            args.putAll(this.getArguments());
            NavController navController = Navigation.findNavController(SignRadioButtonFragment.this.requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_signMethodFragment_to_androidKeystoreSignFragment, args);
        });
    }

    private String getSelectedKeyUri() {
        int id = binding.signRadioGroupKeystores.getCheckedRadioButtonId();
        View selected = binding.signRadioGroupKeystores.findViewById(id);
        String displayName = ((RadioButton) selected).getText().toString();
        return this.displayNameToUri.get(displayName);
    }

    private void makeRadioButtons() {
        for (String displayName: this.displayNameToUri.keySet()) {
            RadioButton rb = new RadioButton(this.getContext());
            rb.setText(displayName);
            this.binding.signRadioGroupKeystores.addView(rb);
        }
    }

    private String makeDisplayName(KeyURI uri) {
        MLog.d("sscd=" + uri.getParam("sscd"));
        MLog.d("provider=" + uri.getParam("provider"));

        if (uri.getParam("sscd").equalsIgnoreCase("external signature") &&
                uri.getParam("provider").equalsIgnoreCase("dna")) {
            return "SIM-based Mobile ID";
        } else if (uri.getParam("sscd").equalsIgnoreCase("Android Keystore")) {
            return "Phone Keystore";
        } else if (uri.getParam("sscd").equalsIgnoreCase("external signature")) {
            return "eIDAS Remote Signing";
        } else if (uri.getParam("provider").equalsIgnoreCase("yubico")) {
            return "Hardware Token";
        } else {
            return uri.getParam("sscd");
        }
    }
}