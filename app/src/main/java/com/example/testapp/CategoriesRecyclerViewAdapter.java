package com.example.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> {

    private List<String> categoryNames;
    private Context categoriesContext;

    public CategoriesRecyclerViewAdapter(List<String> categoryNames, Context categoriesContext) {
        this.categoryNames = categoryNames;
        this.categoriesContext = categoriesContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //set the categories title
        holder.categoryChip.setText(categoryNames.get(position));

        //set click listener
        holder.categoryChip.setOnClickListener(view -> {
            Toast.makeText(categoriesContext, String.format("%s clicked", categoryNames.get(position)), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Chip categoryChip;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.category_chip);
        }
    }
}
