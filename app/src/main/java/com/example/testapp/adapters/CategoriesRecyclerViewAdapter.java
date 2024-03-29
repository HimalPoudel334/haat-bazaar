package com.example.testapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Category;
import com.google.android.material.chip.Chip;

import java.util.List;

public class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> {

    private List<Category> categories;
    private Context categoriesContext;

    public CategoriesRecyclerViewAdapter(List<Category> categories, Context categoriesContext) {
        this.categories = categories;
        Log.d("Inside adapter", "CategoriesRecyclerViewAdapter: "+categories.size());
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
        holder.categoryChip.setText(categories.get(position).getName());

        //set click listener
        holder.categoryChip.setOnClickListener(view -> {
            Toast.makeText(categoriesContext, String.format("%s clicked", categories.get(position).getName()), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Chip categoryChip;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.category_chip);
        }
    }
}
