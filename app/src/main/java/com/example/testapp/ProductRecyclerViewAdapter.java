package com.example.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.models.Product;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private final Context productContext;
    private final List<Product> productList;

    public ProductRecyclerViewAdapter(Context productContext, List<Product> productList) {
        this.productContext = productContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(productContext).inflate(R.layout.product_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //load images
        Glide.with(productContext)
                .load(productList.get(position).getProductImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.productImageView);

        holder.productTitle.setText(productList.get(position).getProductName());
        holder.productDescription.setText(productList.get(position).getProductDescription());
        holder.productPrice.setText(String.format("%s", productList.get(position).getProductPrice()));
        holder.productUnit.setText(productList.get(position).getProductUnit());
        holder.productPreviousPrice.setText(String.format("%s", productList.get(position).getProductPreviousPrice()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView productImageView;
        private final TextView productTitle;
        private final TextView productDescription;
        private final TextView productPrice;
        private final TextView productUnit;
        private final TextView productPreviousPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.product_image_view);
            productTitle = itemView.findViewById(R.id.product_title);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
            productUnit = itemView.findViewById(R.id.product_unit);
            productPreviousPrice = itemView.findViewById(R.id.product_previous_price);
        }
    }
}
