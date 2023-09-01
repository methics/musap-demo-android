package fi.methics.musap.ui.keygen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fi.methics.musap.MUSAPClientHolder;
import fi.methics.musap.databinding.FragmentKeygenBinding;
import fi.methics.musap.sdk.MUSAPSscdType;
import fi.methics.musap.sdk.api.MUSAPSscd;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keygeneration.KeyGenReqBuilder;
import fi.methics.musap.sdk.util.MLog;

public class KeygenFragment extends Fragment {

    private FragmentKeygenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentKeygenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button generate = binding.buttonGenerate;

        // TODO: Read the type from radio buttons
        final MUSAPSscdType type = MUSAPSscdType.PHONE_KEYSTORE;

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alias = binding.edittextAlias.getText().toString();
                MLog.d("Alias=" + alias);

                KeyGenReq req = new KeyGenReqBuilder()
                        .setAlias(alias)
                        .setSscd(new MUSAPSscd(type))
                        .createKeyGenReq();

                MUSAPClientHolder.getClient().generateKey(req);

                Toast.makeText(KeygenFragment.this.getContext(), "Generated key " + alias, Toast.LENGTH_SHORT).show();

                // Reset text
                binding.edittextAlias.getText().clear();
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