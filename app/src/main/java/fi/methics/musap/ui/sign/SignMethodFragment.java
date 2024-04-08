package fi.methics.musap.ui.sign;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.internal.datatype.KeyURI;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.util.MLog;

/**
 * SignMethodFragments lists all methods the user can use to sign data.
 */
public class SignMethodFragment extends Fragment {

    private static final int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SignMethodFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_key_list, container, false);

        View list = view.findViewById(R.id.list_sign_keys);

        final Bundle args = getArguments();

        MLog.d("SignMethodFragment created");
        // Set the adapter
        if (list instanceof RecyclerView) {
            Context context = list.getContext();
            RecyclerView recyclerView = (RecyclerView) list;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            List<KeyURI> keyNames = MusapClient.listKeys().stream().map(MusapKey::getKeyUri).collect(Collectors.toList());
            List<String> keys = new ArrayList<>();

            MLog.d("Found keys: ");
            for (KeyURI keyuri : keyNames) {
                MLog.d("  " + keyuri);
                keys.add(this.makeDisplayName(keyuri));
            }

            NavController navController = Navigation.findNavController(SignMethodFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
            recyclerView.setAdapter(new SignMethodRecyclerViewAdapter(keys, keyNames, navController, args));
        }
        return view;
    }

    /**
     * Make a pretty display name for different keystores
     * @param uri
     * @return
     */
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