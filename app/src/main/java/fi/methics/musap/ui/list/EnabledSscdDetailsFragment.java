package fi.methics.musap.ui.list;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import fi.methics.musap.R;
import fi.methics.musap.databinding.FragmentEnabledSscdDetailsBinding;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.api.MusapConstants;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.util.MLog;

public class EnabledSscdDetailsFragment extends Fragment {

    private FragmentEnabledSscdDetailsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEnabledSscdDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Bundle args = getArguments();

        final String sscdid = args.getString(MusapConstants.SSCD_ID);
        if (sscdid == null) {
            MLog.d("Cannot handle null SSCD ID");
            return root;
        }
        MusapSscdInterface sscd = null;
        for (MusapSscdInterface s : MusapClient.listEnabledSSCDS()) {
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
            NavController navController = Navigation.findNavController(EnabledSscdDetailsFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
            binding.viewKeystoreDetails.setAdapter(new EnabledSscdDetailsViewAdapter(sscd));
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