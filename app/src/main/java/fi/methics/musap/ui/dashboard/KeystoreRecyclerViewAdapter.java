package fi.methics.musap.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.methics.musap.R;
import fi.methics.musap.databinding.FragmentKeystoreBinding;
import fi.methics.musap.sdk.keyuri.KeyURI;

public class KeystoreRecyclerViewAdapter extends RecyclerView.Adapter<KeystoreRecyclerViewAdapter.ViewHolder> {

    private final List<KeyURI> mValues;

    private final int windowWidth;

    private final Context c;

    private final NavController controller;

    public KeystoreRecyclerViewAdapter(List<KeyURI> items, int windowWidth,
                                       Context c, NavController navController) {
        mValues = items;
        this.windowWidth = windowWidth;
        this.c = c;
        this.controller = navController;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentKeystoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
//        holder.mContentView.setText(mValues.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public KeyURI mItem;

        public ViewHolder(FragmentKeystoreBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;

            mIdView.getLayoutParams().width = KeystoreRecyclerViewAdapter.this.windowWidth / 2;
            mContentView.getLayoutParams().width = KeystoreRecyclerViewAdapter.this.windowWidth / 2;

            mIdView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = mIdView.getText().toString();
                    switch (text) {
                        case "MobileID":
                            controller.navigate(R.id.action_keystoreFragment_to_mobileIdDiscoveryFragment);
                            break;
                        case "RemoteSign":
                            controller.navigate(R.id.action_keystoreFragment_to_remoteSignDiscoveryFragment);
                            break;
                        case "Android Keystore":
                            controller.navigate(R.id.action_keystoreFragment_to_androidKeystoreFragment);
                            break;
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}