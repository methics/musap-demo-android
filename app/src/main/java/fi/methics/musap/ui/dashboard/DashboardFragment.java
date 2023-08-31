package fi.methics.musap.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import fi.methics.musap.R;
import fi.methics.musap.databinding.FragmentDashboardBinding;
import fi.methics.musap.sdk.api.MUSAPConstants;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final Button listButton = binding.buttonListKeystores;
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton any = binding.radioLoaAny;
                RadioButton high = binding.radioLoaHigh;
                RadioButton substantial = binding.radioLoaSubstantial;

                String loa;
                if (any.isChecked()) {
                    loa = "any";
                } else if (high.isChecked()) {
                    loa = "high";
                } else if (substantial.isChecked()) {
                    loa = "substantial";
                } else {
                    loa = null;
                }

                NavController navController = Navigation.findNavController(DashboardFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
                Bundle args = new Bundle();
                args.putString(MUSAPConstants.LoA, loa);
                navController.navigate(R.id.action_navigation_dashboard_to_keystoreFragment, args);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}