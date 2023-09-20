package fi.methics.musap.ui.list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;

/**
 * A fragment representing a list of Items.
 */
public class KeystoreListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public KeystoreListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static KeystoreListFragment newInstance(int columnCount) {
        KeystoreListFragment fragment = new KeystoreListFragment();
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
        View view = inflater.inflate(R.layout.fragment_keystore_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int width = displaymetrics.widthPixels;

            NavController navController = Navigation.findNavController(KeystoreListFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);

            List<MUSAPSscdInterface> sscds = MUSAPClient.listSSCDS();
            recyclerView.setAdapter(new KeystoreRecyclerViewAdapter(sscds, width, this.getContext(), navController));
        }
        return view;
    }

}