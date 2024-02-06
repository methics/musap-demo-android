package fi.methics.musap.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.List;

import fi.methics.musap.databinding.FragmentSscdDetailBinding;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.internal.datatype.SscdInfo;
import fi.methics.musap.sdk.internal.discovery.SscdSearchReq;
import fi.methics.musap.sdk.internal.util.MusapSscd;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SscdDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SscdDetailFragment extends Fragment {

    private FragmentSscdDetailBinding binding;

    private static final String SSCD_ID = "sscdid";

    private String sscdId;

    public SscdDetailFragment() {
        // Required empty public constructor
    }

    public static SscdDetailFragment newInstance(String sscdId) {
        SscdDetailFragment fragment = new SscdDetailFragment();
        Bundle args = new Bundle();
        args.putString(SSCD_ID, sscdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sscdId = getArguments().getString(SSCD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSscdDetailBinding.inflate(inflater);

        SscdSearchReq req = new SscdSearchReq.Builder()
                .setSscd(new SscdInfo.Builder().setSscdId(this.sscdId).build())
                .build();

        List<MusapSscd> found =  MusapClient.listEnabledSscds(req);

        if (found.isEmpty()) {
            Log.d("sscd", "Cannot find SSCD with id " + this.sscdId);
            return binding.getRoot();
        }

        MusapSscd sscd = found.get(0);
        SscdInfo info = sscd.getSscdInfo();
        binding.textSscdCountryVal.setText(info.getCountry());
        binding.textSscdTypeVal.setText(info.getSscdType());
        binding.textSscdProviderVal.setText(info.getProvider());
        binding.textSscdNameVal.setText(info.getSscdName());

        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}