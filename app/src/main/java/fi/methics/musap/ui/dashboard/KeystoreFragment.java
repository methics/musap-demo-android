package fi.methics.musap.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.api.MUSAPConstants;
import fi.methics.musap.sdk.keydiscovery.KeyDiscoveryCriteria;
import fi.methics.musap.sdk.keyuri.KeyURI;

/**
 * A fragment representing a list of Items.
 */
public class KeystoreFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public KeystoreFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static KeystoreFragment newInstance(int columnCount) {
        KeystoreFragment fragment = new KeystoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(KeystoreFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.action_keystoreFragment_to_navigation_dashboard);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keystore_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            Map<KeyDiscoveryCriteria, String> criteria = new HashMap<>();
            Bundle args = getArguments();
            if (args != null) {
                if (args.containsKey(MUSAPConstants.LoA)) {
                    criteria.put(KeyDiscoveryCriteria.LEVEL_OF_ASSURANCE, args.getString(MUSAPConstants.LoA));
                }
            }

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int width = displaymetrics.widthPixels;

            NavController navController = Navigation.findNavController(KeystoreFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);

            List<KeyURI> uriList = new MUSAPClient().listMatchingMethods(criteria);
            recyclerView.setAdapter(new KeystoreRecyclerViewAdapter(uriList, width, this.getContext(), navController));
        }
        return view;
    }

}