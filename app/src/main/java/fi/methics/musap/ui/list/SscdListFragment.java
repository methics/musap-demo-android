package fi.methics.musap.ui.list;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.stream.Collectors;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.util.MLog;

/**
 * A fragment representing a list of Items.
 */
public class SscdListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SscdListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SscdListFragment newInstance(int columnCount) {
        SscdListFragment fragment = new SscdListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        //    @Override
        //    public void handleOnBackPressed() {
        //        NavController navController = Navigation.findNavController(KeystoreFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
        //        navController.navigate(R.id.action_keystoreFragment_to_navigation_dashboard);
        //    }
        //};
        //requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_sscd_list, container, false);

        // Set the adapter
        if (layout instanceof ConstraintLayout) {

            NavController navController = Navigation.findNavController(SscdListFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);

            MLog.d("Listing Enabled SSCDs");
            RecyclerView enabledList = layout.findViewById(R.id.listEnabled);
            enabledList.setLayoutManager(new GridLayoutManager(enabledList.getContext(), mColumnCount));
            enabledList.setAdapter(new EnabledSscdListViewAdapter(MUSAPClient.listEnabledSSCDS(), this.getContext(), navController));

            MLog.d("Listing Active SSCDs");
            RecyclerView activeList = layout.findViewById(R.id.listActive);
            activeList.setLayoutManager(new GridLayoutManager(activeList.getContext(), mColumnCount));
            activeList.setAdapter(new ActiveSscdListViewAdapter(MUSAPClient.listActiveSSCDS(), this.getContext(), navController));

            MLog.d("ACTIVE SSCDs: " + String.join(",", MUSAPClient.listActiveSSCDS().stream().map(s -> s.getSscdId()).collect(Collectors.toList()).toArray(new String[0])));

        }
        return layout;
    }

}