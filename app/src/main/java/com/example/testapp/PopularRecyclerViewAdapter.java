package com.example.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.models.PopularProduct;

import java.util.List;

public class PopularRecyclerViewAdapter extends RecyclerView.Adapter<PopularRecyclerViewAdapter.ViewHolder>{

    //variables
    private List<PopularProduct> popularProducts;
    private Context popularImageContext;

    public PopularRecyclerViewAdapter(Context popularImageContext, List<PopularProduct> popularProducts) {
        this.popularProducts = popularProducts;
        this.popularImageContext = popularImageContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Toast.makeText(popularImageContext, "Inside recycler view", Toast.LENGTH_SHORT);
        //load images
        Glide.with(popularImageContext)
                .load(popularProducts.get(position).getImageUrl())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.popularImage);

        //load titles
        holder.productTitle.setText(popularProducts.get(position).getImageTitle());

        //set click listener
        holder.popularImage.setOnClickListener(view -> {
            Toast.makeText(popularImageContext, String.format("%s clicked", popularProducts.get(position).getImageTitle()), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return popularProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView popularImage;
        TextView productTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            popularImage = itemView.findViewById(R.id.popular_image_view);
            productTitle = itemView.findViewById(R.id.product_title);
        }


    }
}
