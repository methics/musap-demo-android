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
import fi.methics.musap.sdk.keygeneration.KeyGenReq;
import fi.methics.musap.sdk.keygeneration.KeyGenReqBuilder;
import fi.methics.musap.sdk.util.MLog;
import fi.methics.musap.sdk.yubikey.YubiKeyExtension;

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

//        Button yubiTest = binding.buttonYubitest;
//
//        yubiTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                YubiKeyExtension extension = new YubiKeyExtension(HomeFragment.this.getActivity());
//                try {
//                    extension.generateKey(new KeyGenReqBuilder().createKeyGenReq());
//                } catch (Exception e) {
//                    MLog.e("Failed to generate", e);
//                }
//            }
//        });
//
//
//        Button yubiSign = binding.buttonYubisign;
//        yubiSign.setOnClickListener(view -> {
//            YubiKeyExtension extension = new YubiKeyExtension(HomeFragment.this.getActivity());
//            try {
//                extension.sign();
//            } catch (Exception e) {
//                MLog.e("Failed to generate", e);
//            }
//        });

        return root;
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