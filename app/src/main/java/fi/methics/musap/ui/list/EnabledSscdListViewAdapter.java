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
import fi.methics.musap.databinding.FragmentSscdBinding;
import fi.methics.musap.sdk.api.MusapConstants;
import fi.methics.musap.sdk.extension.MusapSscdInterface;

public class EnabledSscdListViewAdapter extends RecyclerView.Adapter<EnabledSscdListViewAdapter.ViewHolder> {

    private final List<MusapSscdInterface> mValues;

    private final Context context;

    private final NavController controller;

    public EnabledSscdListViewAdapter(List<MusapSscdInterface> items, Context context, NavController navController) {
        this.mValues     = items;
        this.controller  = navController;
        this.context     = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentSscdBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getSscdInfo().getSscdName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public MusapSscdInterface mItem;

        public ViewHolder(FragmentSscdBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;

            mIdView.setOnClickListener(view -> {
                Bundle args = new Bundle();
                args.putString(MusapConstants.SSCD_ID, mItem.getSscdInfo().getSscdId());
                controller.navigate(R.id.action_keystoreFragment_to_keystoreDetailsFragment, args);
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}