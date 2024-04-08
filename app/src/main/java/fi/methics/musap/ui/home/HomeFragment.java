package fi.methics.musap.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import fi.methics.musap.databinding.FragmentHomeBinding;
import fi.methics.musap.sdk.api.MusapCallback;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.internal.datatype.RelyingParty;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.button.setOnClickListener(view -> askCouplingCode());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void askCouplingCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.requireActivity());
        builder.setTitle("Enter coupling code");
        final EditText input = new EditText(this.requireActivity());

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> couple(input.getText().toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void couple(String couplingCode) {
        MusapClient.coupleWithRelyingParty(couplingCode, new MusapCallback<RelyingParty>() {
            @Override
            public void onSuccess(RelyingParty relyingParty) {
                Toast.makeText(HomeFragment.this.requireActivity(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(MusapException e) {
                Toast.makeText(HomeFragment.this.requireActivity(), "Coupling failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}