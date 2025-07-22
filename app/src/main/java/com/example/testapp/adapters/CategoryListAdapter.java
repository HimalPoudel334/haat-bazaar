package com.example.testapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.apis.CategoryAPI;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.models.Category;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.requestmodels.CategoryCreate;
import com.example.testapp.responses.CategoryResponses;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder> {

    private final Context context;
    private final List<Category> categories;

    public CategoryListAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);
        return new CategoryListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getName());

        holder.editButton.setOnClickListener(v -> {
            ViewGroup parentLayout = (ViewGroup) holder.categoryName.getParent();
            int nameIndex = parentLayout.indexOfChild(holder.categoryName);
            int buttonIndex = parentLayout.indexOfChild(holder.editButton);

            parentLayout.removeView(holder.categoryName);
            parentLayout.removeView(holder.editButton);

            EditText editText = new EditText(context);
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
            editText.setText(category.getName());
            editText.requestFocus();

            Button saveButton = new Button(context);
            saveButton.setText(R.string.save);

            Button cancelButton = new Button(context);
            cancelButton.setText(R.string.cancel);

            parentLayout.addView(editText, nameIndex);
            parentLayout.addView(saveButton, buttonIndex);
            parentLayout.addView(cancelButton, buttonIndex + 1);

            saveButton.setOnClickListener(saveView -> {
                String newName = editText.getText().toString().trim();
                category.setName(newName);
                //call update api
                updateCategory(newName, category, holder.getAdapterPosition());

                holder.categoryName.setText(category.getName());

                parentLayout.removeView(editText);
                parentLayout.removeView(saveButton);
                parentLayout.removeView(cancelButton);

                parentLayout.addView(holder.categoryName, nameIndex);
                parentLayout.addView(holder.editButton, buttonIndex);
            });

            cancelButton.setOnClickListener(cancelView -> {
                parentLayout.removeView(editText);
                parentLayout.removeView(saveButton);
                parentLayout.removeView(cancelButton);

                parentLayout.addView(holder.categoryName, nameIndex);
                parentLayout.addView(holder.editButton, buttonIndex);
            });

            holder.itemView.post(() -> {
                RecyclerView recyclerView = (RecyclerView) holder.itemView.getParent();
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(holder.getAdapterPosition());
                }
            });

        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryListViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        Button editButton;

        public CategoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryNameTv);
            editButton = itemView.findViewById(R.id.edit);
        }
    }

    private void updateCategory(String newName, Category category, int position) {
        RetrofitClient
            .getAuthClient(AuthManager.getInstance().getAccessToken())
            .create(CategoryAPI.class)
            .updateCategory(category.getId(), new CategoryCreate(newName))
            .enqueue(new retrofit2.Callback<CategoryResponses.SingleCategoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponses.SingleCategoryResponse> call, @NonNull Response<CategoryResponses.SingleCategoryResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        notifyItemChanged(position, response.body().getCategory());
                        Toast.makeText(context, "Category updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponses.SingleCategoryResponse> call, @NonNull Throwable t) {
                    Log.d("Category Edit", "onFailure: " + t.getMessage());
                }
            });
    }
}
