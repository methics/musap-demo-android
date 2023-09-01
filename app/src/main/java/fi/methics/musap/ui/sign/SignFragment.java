package fi.methics.musap.ui.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fi.methics.musap.R;
import fi.methics.musap.sdk.api.MUSAPConstants;
import fi.methics.musap.ui.dashboard.DashboardFragment;

public class SignFragment extends Fragment {


    public SignFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_sign, container, false);

        Button b = v.findViewById(R.id.button_sign_next);

        b.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(SignFragment.this.getActivity(), R.id.nav_host_fragment_activity_main);
            Bundle args = new Bundle();
            navController.navigate(R.id.action_navigation_sign_to_signMethodFragment, args);
        });

        return v;
    }
}