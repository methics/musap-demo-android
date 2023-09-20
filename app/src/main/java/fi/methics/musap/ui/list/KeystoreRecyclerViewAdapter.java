package fi.methics.musap.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.methics.musap.R;
import fi.methics.musap.databinding.FragmentKeystoreBinding;
import fi.methics.musap.sdk.api.MUSAPConstants;
import fi.methics.musap.sdk.extension.MUSAPSscdInterface;

public class KeystoreRecyclerViewAdapter extends RecyclerView.Adapter<KeystoreRecyclerViewAdapter.ViewHolder> {

    private final List<MUSAPSscdInterface> mValues;

    private final int windowWidth;

    private final Context context;

    private final NavController controller;

    public KeystoreRecyclerViewAdapter(List<MUSAPSscdInterface> items, int windowWidth,
                                       Context context, NavController navController) {
        this.mValues     = items;
        this.windowWidth = windowWidth;
        this.controller  = navController;
        this.context     = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentKeystoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        public MUSAPSscdInterface mItem;

        public ViewHolder(FragmentKeystoreBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;

            mIdView.getLayoutParams().width      = KeystoreRecyclerViewAdapter.this.windowWidth / 2;
            mContentView.getLayoutParams().width = KeystoreRecyclerViewAdapter.this.windowWidth / 2;

            mIdView.setOnClickListener(view -> {
                String text = mIdView.getText().toString();
                Bundle args = new Bundle();
                args.putString(MUSAPConstants.SSCD_ID, text);
                controller.navigate(R.id.action_keystoreFragment_to_keystoreDetailsFragment, args);
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}