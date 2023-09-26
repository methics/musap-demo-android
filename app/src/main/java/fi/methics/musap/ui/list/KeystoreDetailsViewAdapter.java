package fi.methics.musap.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.methics.musap.R;
import fi.methics.musap.databinding.FragmentKeystoreBinding;
import fi.methics.musap.sdk.api.MUSAPConstants;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;
import fi.methics.musap.sdk.util.MLog;

public class KeystoreDetailsViewAdapter extends RecyclerView.Adapter<KeystoreDetailsViewAdapter.ViewHolder> {

    private final MUSAPSscdInterface sscd;


    public KeystoreDetailsViewAdapter(MUSAPSscdInterface sscd) {
        MLog.d("new KeystoreDetailsViewAdapter()");
        this.sscd = sscd;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MLog.d("Creating ViewHolder for " + this.sscd.getSscdInfo().getSscdId());
        return new ViewHolder(FragmentKeystoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MLog.d("Binding ViewHolder for " + this.sscd.getSscdInfo().getSscdId() + " on index " + position);
        switch (position) {
            case 0: holder.mIdView.setText("SSCD Name"); break;
            case 1: holder.mIdView.setText(sscd.getSscdInfo().getSscdName()); break;
            case 2: holder.mIdView.setText("SSCD Type"); break;
            case 3: holder.mIdView.setText(sscd.getSscdInfo().getSscdType()); break;
            case 4: holder.mIdView.setText("SSCD Provider");break;
            case 5: holder.mIdView.setText(sscd.getSscdInfo().getProvider());break;
            case 6: holder.mIdView.setText("Country");break;
            case 7: holder.mIdView.setText(sscd.getSscdInfo().getCountry());break;
            case 8: holder.mIdView.setText("Algorithms");break;
            case 9: holder.mIdView.setText(String.join(",", sscd.getSscdInfo().getSupportedKeyAlgorithms()));break;
        }

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(FragmentKeystoreBinding binding) {
            super(binding.getRoot());
            mIdView      = binding.itemNumber;
            mContentView = binding.content;
        }
    }
}