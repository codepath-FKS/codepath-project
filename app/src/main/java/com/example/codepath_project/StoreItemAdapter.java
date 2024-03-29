package com.example.codepath_project;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import fragments.StoreFragment;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.ViewHolder> {

    private String[] mData;
    private List<StoreItem> sData;
    private ArrayList<Integer> purchases;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;



    // data is passed into the constructor
    public StoreItemAdapter(Context context, List<StoreItem> data, ArrayList<Integer> purchases) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.sData = data;
        this.purchases = purchases;

    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_store, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StoreItem storeItem = sData.get(position);
        holder.bind(storeItem);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return sData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvStoreItemName;
        private TextView tvStoreItemCost;
        private ImageView ivStoreItem;

        ViewHolder(View itemView) {
            super(itemView);
            tvStoreItemName = itemView.findViewById(R.id.tvStoreItemName);
            tvStoreItemCost = itemView.findViewById(R.id.tvStoreItemCost);
            ivStoreItem = itemView.findViewById(R.id.ivStoreItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public void bind(StoreItem storeItem) {
            // bind the post data to the view element
            tvStoreItemName.setText(storeItem.getName());
            tvStoreItemCost.setText(String.valueOf(storeItem.getCost()));
            ivStoreItem.setImageResource(storeItem.getRes());
            // grey it out if its already been purchased
            if (purchases.contains(storeItem.getId())){
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                ivStoreItem.setColorFilter(filter);
                storeItem.setPurchased(true);

            }


        }
    }

    // convenience method for getting data at click position
    public StoreItem getItem(int position) {
        return sData.get(position);
    }

    public void setItemPurchased(int id, int position){
        purchases.add(id);
        notifyItemChanged(position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}