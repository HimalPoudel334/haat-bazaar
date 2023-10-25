package com.example.testapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.HomePageMainRecyclerViewAdapter.NormalProductViewHolder;
import com.example.testapp.models.Product;

import java.util.List;

public class FilteredProductRecyclerViewAdapter extends RecyclerView.Adapter<HomePageMainRecyclerViewAdapter.NormalProductViewHolder> {
    private Context context;
    private List<Product> filteredProductList;

    public FilteredProductRecyclerViewAdapter(Context context, List<Product> filteredProductList) {
        this.context = context;
        this.filteredProductList = filteredProductList;
    }

    @NonNull
    @Override
    public NormalProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new NormalProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NormalProductViewHolder holder, int position) {
        Product filteredProduct = filteredProductList.get(position);
        Glide.with(context)
                .load(filteredProduct.getProductImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.getProductImageView());

        holder.getProductTitle().setText(filteredProduct.getProductName());
        holder.getProductDescription().setText(filteredProduct.getProductDescription());
        holder.getProductPrice().setText(String.format("%s per %s", filteredProduct.getProductPrice(), filteredProduct.getProductUnit()));
        holder.getProductPreviousPrice().setText(String.format("%s per %s", filteredProduct.getProductPreviousPrice(), filteredProduct.getProductUnit()));
        holder.getProductPreviousPrice().setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
