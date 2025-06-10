package com.example.testapp.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.MainActivity;
import com.example.testapp.OrderListActivity;
import com.example.testapp.R;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.models.Entity;
import com.example.testapp.models.Order;

import java.util.ArrayList;
import java.util.List;

public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.EntityViewHolder> {

    private Context context;
    private List<Entity> entityList;
    private final int newOrdersCount;

    public EntityAdapter(Context context, List<Entity> entityList, int newOrdersCount) {
        this.context = context;
        this.entityList = entityList;
        this.newOrdersCount = newOrdersCount;
    }

    @NonNull
    @Override
    public EntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_item_card, parent, false);
        return new EntityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntityViewHolder holder, int position) {
        Entity currentEntity = entityList.get(position);
        holder.entityTitleTextView.setText(currentEntity.getName());
        holder.entityImageView.setImageResource(currentEntity.getImageResourceId());
        if (holder.entityTitleTextView.getText().equals("Orders")) {
            holder.newOrdersCount.setText(String.valueOf(newOrdersCount));
            holder.newOrdersCount.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            switch (holder.entityTitleTextView.getText().toString().toLowerCase()) {
                case "orders":
                    intent = new Intent(context, OrderListActivity.class);
                    context.startActivity(intent);
                    break;
                case "logout":
                    AuthManager.getInstance().logout();
                    if (context instanceof Activity) { // Ensure context is an Activity before casting
                        ((Activity) context).finish(); // Finish the current activity
                    }
                    break;
            }

        });
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    public static class EntityViewHolder extends RecyclerView.ViewHolder {
        public ImageView entityImageView;
        public TextView entityTitleTextView, newOrdersCount;

        public EntityViewHolder(@NonNull View itemView) {
            super(itemView);
            entityImageView = itemView.findViewById(R.id.entity_image_view);
            entityTitleTextView = itemView.findViewById(R.id.entity_title);
            newOrdersCount = itemView.findViewById(R.id.new_orders_count_tv);
        }
    }
}
