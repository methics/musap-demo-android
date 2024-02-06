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
import fi.methics.musap.databinding.FragmentSscdListBinding;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.internal.util.MLog;

/**
 * SscdListFragments lists all user enabled and active SSCDs.
 */
public class SscdListFragment extends Fragment {

    FragmentSscdListBinding binding;

    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SscdListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentSscdListBinding.inflate(inflater);

        NavController navController = Navigation.findNavController(SscdListFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);

        MLog.d("Listing Enabled SSCDs");
        RecyclerView enabledList = binding.listEnabled;
        enabledList.setLayoutManager(new GridLayoutManager(enabledList.getContext(), mColumnCount));
        enabledList.setAdapter(new EnabledSscdListViewAdapter(MusapClient.listEnabledSscds(), this.getContext(), navController));

        MLog.d("Listing Active SSCDs");
        RecyclerView activeList = binding.listActive;
        activeList.setLayoutManager(new GridLayoutManager(activeList.getContext(), mColumnCount));
        activeList.setAdapter(new ActiveSscdListViewAdapter(MusapClient.listActiveSscds(), this.getContext(), navController));

        MLog.d("ACTIVE SSCDs: " + String.join(",", MusapClient.listActiveSscds().stream().map(s -> s.getSscdId()).collect(Collectors.toList()).toArray(new String[0])));

        return this.binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
}