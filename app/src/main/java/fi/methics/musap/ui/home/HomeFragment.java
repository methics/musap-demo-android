package fi.methics.musap.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yubico.yubikit.android.YubiKitManager;
import com.yubico.yubikit.android.transport.nfc.NfcConfiguration;
import com.yubico.yubikit.android.transport.nfc.NfcNotAvailable;
import com.yubico.yubikit.android.transport.nfc.NfcYubiKeyDevice;
import com.yubico.yubikit.core.application.ApplicationNotAvailableException;
import com.yubico.yubikit.core.smartcard.Apdu;
import com.yubico.yubikit.core.smartcard.SmartCardConnection;
import com.yubico.yubikit.core.smartcard.SmartCardProtocol;
import com.yubico.yubikit.piv.KeyType;
import com.yubico.yubikit.piv.ManagementKeyType;
import com.yubico.yubikit.piv.PivSession;
import com.yubico.yubikit.piv.Slot;
import com.yubico.yubikit.piv.jca.PivAlgorithmParameterSpec;
import com.yubico.yubikit.piv.jca.PivProvider;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.Signature;

import fi.methics.musap.databinding.FragmentHomeBinding;
import fi.methics.musap.sdk.util.MLog;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private YubiKitManager yubiKitManager;

    private static final byte[] MANAGEMENT_KEY = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final ManagementKeyType TYPE = ManagementKeyType.TDES;
    private static final char[] DEFAULT_PIN = "123456".toCharArray();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        Button yubiTest = binding.buttonYubitest;

        yubiTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context c = HomeFragment.this.getContext();

                if (yubiKitManager == null) {
                    yubiKitManager = new YubiKitManager(c);
                }

                Toast.makeText(c, "Looking for Yubikey...", Toast.LENGTH_SHORT).show();

                try {
                    yubiKitManager.startNfcDiscovery(new NfcConfiguration(), HomeFragment.this.getActivity(), device -> {
//                        Toast.makeText(c, "Found NFC", Toast.LENGTH_SHORT).show();
                        MLog.d("Found NFC");

                        connect(device);

                    });
                } catch (NfcNotAvailable e) {
                    if (e.isDisabled()) {
                        Toast.makeText(c, "NFC is not enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(c, "NFC is not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void connect(NfcYubiKeyDevice device)  {
        device.requestConnection(SmartCardConnection.class, result -> {
            // The result is a Result<SmartCardConnection, IOException>, which represents either a successful connection, or an error.
            try {
                SmartCardConnection connection = result.getValue();  // This may throw an IOException
                PivSession pivSession = new PivSession(connection);

                pivSession.authenticate(TYPE, MANAGEMENT_KEY);

                PivProvider pivProvider = new PivProvider(pivSession);
                Security.insertProviderAt(pivProvider, 1); // JCA Security providers are indexed from 1

                KeyPairGenerator ecKpg = KeyPairGenerator.getInstance("YKPivEC");
                MLog.d("Initialized KeyPairGenerator");

                ecKpg.initialize(
                        new PivAlgorithmParameterSpec(
                                Slot.AUTHENTICATION,
                                KeyType.ECCP384,
                                null, // PinPolicy
                                null, // TouchPolicy
                                DEFAULT_PIN // PIV PIN
                        )
                );
                ecKpg.generateKeyPair();

                MLog.d("Generated KeyPair");

            } catch(Exception e) {
                MLog.e("Failed to connect", e);
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}