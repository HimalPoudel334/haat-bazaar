package com.example.testapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.testapp.EditProductActivity;
import com.example.testapp.ProductDetailActivity;
import com.example.testapp.R;
import com.example.testapp.models.Product;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<HomePageMainRecyclerViewAdapter.NormalProductViewHolder> {
    private final Context context;
    private final List<Product> products;

    public ProductListAdapter(Context context, List<Product> product) {
        this.context = context;
        this.products = product;
    }

    @NonNull
    @Override
    public HomePageMainRecyclerViewAdapter.NormalProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new HomePageMainRecyclerViewAdapter.NormalProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageMainRecyclerViewAdapter.NormalProductViewHolder  holder, int position) {
        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .centerCrop()
                .signature(new ObjectKey(System.currentTimeMillis())) // unique key to break cache
                .into(holder.productImageView);

        holder.productTitle.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(String.format("Rs %s per %s", product.getPrice(), product.getUnit()));
        holder.productPreviousPrice.setText(String.format("Rs %s per %s", product.getPreviousPrice(), product.getUnit()));
        holder.productPreviousPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        //set onclick listener for the product card
        holder.productCardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("selectedProduct", product);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        holder.productCardView.setOnLongClickListener(v -> {
            Intent intent = new Intent(context, EditProductActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("selectedProduct", product);
            intent.putExtras(bundle);
            context.startActivity(intent);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
