package fi.methics.musap.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fi.methics.musap.MUSAPClientHolder;
import fi.methics.musap.R;
import fi.methics.musap.sdk.keydiscovery.KeyBindReq;
import fi.methics.musap.sdk.keydiscovery.KeyBindReqBuilder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AndroidKeystoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AndroidKeystoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AndroidKeystoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AndroidKeystoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AndroidKeystoreFragment newInstance(String param1, String param2) {
        AndroidKeystoreFragment fragment = new AndroidKeystoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_android_keystore, container, false);

        Button b = v.findViewById(R.id.button_bind_android);
        if (b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KeyBindReq req = new KeyBindReqBuilder()
                            .setSscd("Android Keystore")
                            .setGenerateNewKey(true)
                            .createKeyBindReq();

                    MUSAPClientHolder.getClient().bindKey(req);
                }
            });
        }

        return inflater.inflate(R.layout.fragment_android_keystore, container, false);
    }
}