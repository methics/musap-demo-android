package fi.methics.musap.ui.sign;

import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fi.methics.musap.R;
import fi.methics.musap.databinding.FragmentSignMethodBinding;

import java.util.List;


public class SignMethodRecyclerViewAdapter extends RecyclerView.Adapter<SignMethodRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;

    protected static final String KEY_URI   = "keyuri";
    protected static final String DTBS      = "dtbs";

    private final NavController controller;
    // Arguments of parent class
    private final Bundle args;


    public SignMethodRecyclerViewAdapter(List<String> items, NavController navController, Bundle args) {
        mValues = items;
        this.controller = navController;
        this.args = args;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentSignMethodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position));
//        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public String mItem;

        public ViewHolder(FragmentSignMethodBinding binding) {
            super(binding.getRoot());
            mIdView      = binding.itemNumber;
            mContentView = binding.content;


            mIdView.setOnClickListener(view -> {
                String keyUri = mItem;

                Bundle args = new Bundle();
                args.putString(KEY_URI, keyUri);
                args.putAll(SignMethodRecyclerViewAdapter.this.args);
                controller.navigate(R.id.action_signMethodFragment_to_androidKeystoreSignFragment, args);
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}