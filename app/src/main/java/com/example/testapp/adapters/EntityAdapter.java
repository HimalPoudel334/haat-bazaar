package com.example.testapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Entity;

import java.util.List;

public class EntityAdapter extends RecyclerView.Adapter<EntityAdapter.EntityViewHolder> {

    private Context context;
    private List<Entity> entityList;

    public EntityAdapter(Context context, List<Entity> entityList) {
        this.context = context;
        this.entityList = entityList;
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
        // You can set an OnClickListener here if you want to handle clicks on the cards
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    public static class EntityViewHolder extends RecyclerView.ViewHolder {
        public ImageView entityImageView;
        public TextView entityTitleTextView;

        public EntityViewHolder(@NonNull View itemView) {
            super(itemView);
            entityImageView = itemView.findViewById(R.id.entity_image_view);
            entityTitleTextView = itemView.findViewById(R.id.entity_title);
        }
    }
}
