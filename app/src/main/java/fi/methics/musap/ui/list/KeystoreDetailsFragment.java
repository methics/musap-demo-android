package fi.methics.musap.ui.list;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import fi.methics.musap.R;
import fi.methics.musap.databinding.FragmentKeystoreDetailsBinding;
import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.api.MUSAPConstants;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.util.MLog;
import fi.methics.musap.ui.sign.SignMethodRecyclerViewAdapter;

public class KeystoreDetailsFragment extends Fragment {

    private FragmentKeystoreDetailsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentKeystoreDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Bundle args = getArguments();

        final String sscdid = args.getString(MUSAPConstants.SSCD_ID);
        if (sscdid == null) {
            MLog.d("Cannot handle null SSCD ID");
            return root;
        }
        MUSAPSscdInterface sscd = null;
        for (MUSAPSscdInterface s : MUSAPClient.listSSCDS()) {
            if (sscdid.equals(s.getSscdInfo().getSscdId())) {
                MLog.d("Found SSCD " + s.getSscdInfo().getSscdName());
                sscd = s;
                break;
            } else {
                MLog.d("SSCD ID " + sscdid + " does not match " + s.getSscdInfo().getSscdId());
            }
        }
        if (sscd != null) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;

            MLog.d("Creating a new KeystoreDetailsViewAdapter");
            NavController navController = Navigation.findNavController(KeystoreDetailsFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
            binding.viewKeystoreDetails.setAdapter(new KeystoreDetailsViewAdapter(sscd));
            binding.viewKeystoreDetails.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        } else {
            MLog.d("No SSCD");
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}