package fi.methics.musap.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.stream.Collectors;

import fi.methics.musap.databinding.FragmentSscdBinding;
import fi.methics.musap.sdk.internal.util.MusapSscd;

public class ActiveSscdDetailsViewAdapter extends RecyclerView.Adapter<ActiveSscdDetailsViewAdapter.ViewHolder> {

    private final MusapSscd sscd;


    public ActiveSscdDetailsViewAdapter(MusapSscd sscd) {
        this.sscd = sscd;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentSscdBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
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
            case 9: holder.mIdView.setText(String.join(",", sscd.getSscdInfo().getSupportedAlgorithms().stream().map(a -> a.toString()).collect(Collectors.toList()))); break;
        }

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(FragmentSscdBinding binding) {
            super(binding.getRoot());
            mIdView      = binding.itemNumber;
            mContentView = binding.content;
        }
    }
}