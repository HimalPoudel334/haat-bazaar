package com.example.testapp.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.models.Product;

import java.util.List;

public class FilteredProductRecyclerViewAdapter extends RecyclerView.Adapter<FilteredProductRecyclerViewAdapter.FilteredProductViewHolder> {
    private Context context;
    private List<Product> filteredProductList;

    public FilteredProductRecyclerViewAdapter(Context context, List<Product> filteredProductList) {
        this.context = context;
        this.filteredProductList = filteredProductList;
    }

    @NonNull
    @Override
    public FilteredProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filtered_product_item, parent, false);
        return new FilteredProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredProductViewHolder holder, int position) {
        Product filteredProduct = filteredProductList.get(position);
        Glide.with(context)
                .load(filteredProduct.getProductImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.filteredProductImageView);

        holder.filteredProductTitle.setText(filteredProduct.getProductName());
        holder.filteredProductDescription.setText(filteredProduct.getProductDescription());
        holder.filteredProductPrice.setText(String.format("%s per %s", filteredProduct.getProductPrice(), filteredProduct.getProductUnit()));
        holder.filteredProductPreviousPrice.setText(String.format("%s per %s", filteredProduct.getProductPreviousPrice(), filteredProduct.getProductUnit()));
        holder.filteredProductPreviousPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return filteredProductList.size();
    }

    public class FilteredProductViewHolder extends RecyclerView.ViewHolder {

        private final ImageView filteredProductImageView;
        private final TextView filteredProductTitle;
        private final TextView filteredProductDescription;
        private final TextView filteredProductPrice;
        private final TextView filteredProductPreviousPrice;

        public FilteredProductViewHolder(@NonNull View itemView) {
            super(itemView);

            filteredProductImageView = itemView.findViewById(R.id.filtered_product_image_view);
            filteredProductTitle = itemView.findViewById(R.id.filtered_product_title);
            filteredProductDescription = itemView.findViewById(R.id.filtered_product_description);
            filteredProductPrice = itemView.findViewById(R.id.filtered_product_price);
            filteredProductPreviousPrice = itemView.findViewById(R.id.filtered_product_previous_price);
        }
    }
}
