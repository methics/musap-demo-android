package fi.methics.musap.ui.keygen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fi.methics.musap.databinding.FragmentKeygenBinding;
import fi.methics.musap.sdk.api.MusapClient;
import fi.methics.musap.sdk.api.MusapException;
import fi.methics.musap.sdk.extension.MusapSscdInterface;
import fi.methics.musap.sdk.internal.datatype.KeyAlgorithm;
import fi.methics.musap.sdk.internal.discovery.KeyBindReq;
import fi.methics.musap.sdk.internal.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.internal.datatype.MusapKey;
import fi.methics.musap.sdk.internal.util.MLog;
import fi.methics.musap.sdk.api.MusapCallback;

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
        Map<RadioButton, MusapSscdInterface<?>> radioButtons = this.createRadiButtons();

        // Set a random key name as default.
        binding.edittextAlias.setText(UUID.randomUUID().toString());

        generate.setOnClickListener(view -> {
            String alias = binding.edittextAlias.getText().toString();
            MLog.d("Alias=" + alias);

            MusapSscdInterface<?> sscd = this.getSelectedSscd(radioButtons);
            if (sscd == null) {
                MLog.d("No SSCD selected");
                return;
            }

            try {
                MLog.d("Generating key");

                if (sscd.isKeygenSupported()) {

                    KeyGenReq req = new KeyGenReq.Builder()
                            .setActivity(this.getActivity())
                            .setView(this.getView())
                            .setRole("personal")
                            .setKeyAlias(alias)
                            .createKeyGenReq();

                    MusapClient.generateKey(sscd, req, new MusapCallback<MusapKey>() {
                        @Override
                        public void onSuccess(MusapKey result) {
                            MLog.d("Successfully generated key " + alias);
                            Toast.makeText(KeygenFragment.this.getContext(), "Generated key", Toast.LENGTH_SHORT).show();
                            binding.edittextAlias.getText().clear();
                        }

                        @Override
                        public void onException(MusapException e) {
                            Toast.makeText(KeygenFragment.this.getContext(), "Failed to generate key: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.edittextAlias.getText().clear();
                            MLog.e("Failed to generate key " + alias, e);
                        }
                    });
                } else {

                    KeyBindReq req = new KeyBindReq.Builder()
                            .setActivity(this.getActivity())
                            .setView(this.getView())
                            .setRole("personal")
                            .setKeyAlias(alias)
                            .createKeyBindReq();

                    MusapClient.bindKey(sscd, req, new MusapCallback<MusapKey>() {
                        @Override
                        public void onSuccess(MusapKey result) {
                            MLog.d("Successfully bound key " + alias);
                            Toast.makeText(KeygenFragment.this.getContext(), "Bound key " + alias, Toast.LENGTH_SHORT).show();
                            binding.edittextAlias.getText().clear();
                        }

                        @Override
                        public void onException(MusapException e) {
                            Toast.makeText(KeygenFragment.this.getContext(), "Failed to bind key: " + e.toString(), Toast.LENGTH_LONG).show();
                            binding.edittextAlias.getText().clear();
                            MLog.e("Failed to bind key " + alias, e);
                        }
                    });
                }
            } catch (Exception e) {
                MLog.e("Failed to generate key", e);
            }

        });

        return root;
    }

    /**
     * Create RadioButtons for each SSCD
     * @return RadioButton to SSCD map
     */
    private Map<RadioButton, MusapSscdInterface<?>> createRadiButtons() {
        final Map<RadioButton, MusapSscdInterface<?>> sscds = new HashMap<>();
        int i = 0;
        MLog.d("Found " + sscds.size() + " SSCDs");

        for (MusapSscdInterface sscd : MusapClient.listEnabledSscds()) {
            i++;
            RadioButton rb = new RadioButton(this.getContext());
            rb.setText(sscd.getSscdInfo().getSscdName());
            rb.setId(100+i);
            MLog.d("Added radio button " + sscd.getSscdInfo().getSscdName() + " with id " + sscd.hashCode());
            binding.radioGroupKeystores.addView(rb);
            sscds.put(rb, sscd);
        }
        return sscds;
    }

    /**
     * Find the selected SSCD
     * @param radioButtons RadioButtons created with {@link #createRadiButtons()}
     * @return
     */
    private MusapSscdInterface<?> getSelectedSscd(Map<RadioButton, MusapSscdInterface<?>> radioButtons) {
        MLog.d("Looking for selected radio button");
        MusapSscdInterface<?> sscd = null;
        for (RadioButton rb : radioButtons.keySet()) {
            if (rb.isChecked()) {
                sscd = radioButtons.get(rb);
                MLog.d(rb.getText() + " is selected");
            }
        }
        return sscd;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}