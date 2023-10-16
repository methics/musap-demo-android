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
import fi.methics.musap.sdk.internal.util.MLog;

/**
 * A fragment representing a list of Items.
 */
public class SignMethodFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SignMethodFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SignMethodFragment newInstance(int columnCount) {
        SignMethodFragment fragment = new SignMethodFragment();
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

            Set<String> keyNames = MusapClient.listKeys().stream().map(key -> key.getKeyUri()).map(uri -> uri.toString()).collect(Collectors.toSet());
            List<String> keys = new ArrayList<>(keyNames);

            MLog.d("Found keys: ");
            for (String keyuri : keys) {
                MLog.d("  " + keyuri);
            }

            NavController navController = Navigation.findNavController(SignMethodFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
            recyclerView.setAdapter(new SignMethodRecyclerViewAdapter(keys, navController, args));
        }
        return view;
    }
}