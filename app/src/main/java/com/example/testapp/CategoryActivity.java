package com.example.testapp;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.CategoryListAdapter;
import com.example.testapp.apis.CategoryAPI;
import com.example.testapp.models.Category;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.CategoryResponses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends BaseActivity {

    private List<Category> categories;
    private CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Categories");

        categories = new ArrayList<>();
        adapter = new CategoryListAdapter(this, categories);

        Button addNew = findViewById(R.id.addNew);
        LinearLayout containerLayout = findViewById(R.id.containerLayout);

        addNew.setOnClickListener(v -> {
            Context context = v.getContext();

            // Prevent multiple input views
            if (containerLayout.findViewWithTag("inputRow") != null) return;

            // Create a horizontal layout for EditText and buttons
            LinearLayout inputLayout = new LinearLayout(context);
            inputLayout.setOrientation(LinearLayout.HORIZONTAL);
            inputLayout.setTag("inputRow"); // So we can identify/remove later
            inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            inputLayout.setPadding(10, 10, 10, 10);

            // EditText
            EditText editText = new EditText(context);
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
            editText.setHint("Enter new category");

            // Add button
            Button addButton = new Button(context);
            addButton.setText(R.string.add_new);
            addButton.setEnabled(false);

            // Cancel button
            Button cancelButton = new Button(context);
            cancelButton.setText(R.string.cancel);

            // Add views to layout
            inputLayout.addView(editText);
            inputLayout.addView(cancelButton);
            inputLayout.addView(addButton);

            // Insert input layout just after addNew button
            int index = containerLayout.indexOfChild(addNew);
            containerLayout.addView(inputLayout, index + 1);

            // TextWatcher to enable/disable Add button
            editText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    addButton.setEnabled(!s.toString().trim().isEmpty());
                }
                @Override public void afterTextChanged(Editable s) {}
            });

            // Handle Add
            addButton.setOnClickListener(av -> {
                String newItem = editText.getText().toString().trim();
                if (!newItem.isEmpty()) {
                    addButton.setEnabled(true);
                    createCategory(newItem);
                }
                containerLayout.removeView(inputLayout);
            });

            // Handle Cancel
            cancelButton.setOnClickListener(cv -> {
                hideKeyboard(editText);
                containerLayout.removeView(inputLayout);
            });

            // Show keyboard immediately
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        setupMainContent();
    }

    private void setupMainContent() {
        RetrofitClient
                .getClient()
                .create(CategoryAPI.class)
                .getCategories()
                .enqueue(new Callback<CategoryResponses.MultiCategoryResponse>() {

            @Override
            public void onResponse(Call<CategoryResponses.MultiCategoryResponse> call, Response<CategoryResponses.MultiCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int startIndex = categories.size();
                    List<Category> newCategories = response.body().getCategories();
                    categories.addAll(newCategories);
                    adapter.notifyItemRangeInserted(startIndex, newCategories.size());

                    RecyclerView crv = findViewById(R.id.category_list_rv);
                    crv.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
                    crv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponses.MultiCategoryResponse> call, Throwable t) {

            }
        });
    }

    private void createCategory(String name) {
        Category.CategoryCreate category = new Category.CategoryCreate(name);
        RetrofitClient
                .getAuthClient(getUserToken())
                .create(CategoryAPI.class)
                .createCategory(category)
                .enqueue(new Callback<CategoryResponses.SingleCategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponses.SingleCategoryResponse> call, Response<CategoryResponses.SingleCategoryResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            categories.add(response.body().getCategory());
                            adapter.notifyItemInserted(categories.size() - 1);
                        }
                        else {
                            try {
                                Log.d("Category Create", "onResponse: " + response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponses.SingleCategoryResponse> call, Throwable t) {
                        Log.d("Category", "onFailure: " + t.getMessage());
                    }
                });

    }

    // Method to hide keyboard
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}