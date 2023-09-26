package fi.methics.musap.ui.keygen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.methics.musap.MUSAPClientHolder;
import fi.methics.musap.databinding.FragmentKeygenBinding;
import fi.methics.musap.sdk.MUSAPSscdType;
import fi.methics.musap.sdk.api.MUSAPClient;
import fi.methics.musap.sdk.api.MUSAPSscd;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.keydiscovery.KeyMetaDataStorage;
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keygeneration.KeyGenReqBuilder;
import fi.methics.musap.sdk.keyuri.MUSAPKey;
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

        MLog.d("Keygen Fragment created");

        // TODO: Read the type from radio buttons
        final MUSAPSscdType type = MUSAPSscdType.PHONE_KEYSTORE;
        final Map<RadioButton, MUSAPSscdInterface> sscds = new HashMap<>();
        int i = 0;
        MLog.d("Found " + sscds.size() + " SSCDs");
        for (MUSAPSscdInterface sscd : MUSAPClient.listSSCDS()) {
            if (!sscd.isKeygenSupported()) continue;
            i++;
            RadioButton rb = new RadioButton(this.getContext());
            rb.setText(sscd.getSscdInfo().getSscdName());
            rb.setId(100+i);
            MLog.d("Added radio button " + sscd.getSscdInfo().getSscdName() + " with id " + sscd.hashCode());
            binding.radioGroupKeystores.addView(rb);
            sscds.put(rb, sscd);
        }

        generate.setOnClickListener(view -> {
            String alias = binding.edittextAlias.getText().toString();
            MLog.d("Alias=" + alias);

            KeyGenReq req = new KeyGenReqBuilder()
                    .setActivity(this.getActivity())
                    .setAlias(alias)
                    .createKeyGenReq();

            MLog.d("Looking for selected radio button");
            MUSAPSscdInterface sscd = null;
            for (RadioButton rb : sscds.keySet()) {
                if (rb.isChecked()) {
                    sscd = sscds.get(rb);
                    MLog.d(rb.getText() + " is selected");
                }
            }
            if (sscd == null) {
                MLog.d("No SSCD selected");
                return;
            }
            try {
                MLog.d("Generating key");
                MUSAPKey key = sscd.generateKey(req);
                new KeyMetaDataStorage(KeygenFragment.this.getContext()).storeKey(key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //MUSAPClientHolder.getClient().generateKey(req);
            Toast.makeText(KeygenFragment.this.getContext(), "Generated key " + alias, Toast.LENGTH_SHORT).show();

            // Reset text
            binding.edittextAlias.getText().clear();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}