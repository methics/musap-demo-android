package fi.methics.musap.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yubico.yubikit.android.YubiKitManager;
import com.yubico.yubikit.piv.ManagementKeyType;

import fi.methics.musap.databinding.FragmentHomeBinding;

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