package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.helpers.BaseTypeHelper;
import com.example.testapp.helpers.ImagePickerHelper;
import com.example.testapp.apis.CategoryAPI;
import com.example.testapp.apis.ProductAPI;
import com.example.testapp.models.Category;
import com.example.testapp.models.Product;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.CategoryResponses;
import com.example.testapp.responses.ProductResponses;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends BaseActivity {

    private ImagePickerHelper imagePickerHelper;
    private Uri imageUri;
    private Spinner categorySpinner, productSkuSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Add Product");

        categorySpinner = findViewById(R.id.category_spinner);

        imagePickerHelper = new ImagePickerHelper(this);

        setupMainContent();
        setupCategoriesDropdown();
    }

    private void setupMainContent() {
        ImageView imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(v1 -> {
            imagePickerHelper.pickSingleImage((uri) -> {
                Log.d("Product Add", "setupMainContent: " + uri.toString());
                imageUri = uri;
                imageView.setImageURI(uri);
            });
        });

        productSkuSpinner = findViewById(R.id.product_sku_spinner);

        findViewById(R.id.save_button).setOnClickListener(v -> {
            String name = ((EditText) findViewById(R.id.product_name_et)).getText().toString().trim();
            String description = ((EditText) findViewById(R.id.product_description_et)).getText().toString().trim();
            double price = Double.parseDouble(((EditText) findViewById(R.id.product_price_et)).getText().toString().trim());
            double previousPrice = Double.parseDouble(((EditText) findViewById(R.id.product_previous_price_et)).getText().toString().trim());
            double stock = Double.parseDouble(((EditText) findViewById(R.id.product_stock_et)).getText().toString().trim());
            String sku = productSkuSpinner.getSelectedItem().toString().trim();
            double unitChange = Double.parseDouble(((EditText) findViewById(R.id.product_unit_change_et)).getText().toString().trim());
            Category category = (Category) categorySpinner.getSelectedItem();

            Product product = new Product(name, "", description, price, previousPrice, sku, unitChange, stock, category);

            createProduct(product);
        });

        setupProductSkuSpinner();
    }

    private void setupCategoriesDropdown() {
        RetrofitClient
                .getClient()
                .create(CategoryAPI.class)
                .getCategories()
                .enqueue(new Callback<CategoryResponses.MultiCategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponses.MultiCategoryResponse> call, Response<CategoryResponses.MultiCategoryResponse> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                                    AddProductActivity.this, android.R.layout.simple_spinner_item, response.body().getCategories()
                            );
                            categorySpinner.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponses.MultiCategoryResponse> call, Throwable t) {

                    }
                });
    }

    private void createProduct(Product product) {
        RequestBody name = RequestBody.create(product.getName(), MediaType.parse("text/plain"));
        RequestBody description = RequestBody.create(product.getDescription(), MediaType.parse("text/plain"));
        RequestBody price = RequestBody.create(String.valueOf(product.getPrice()), MediaType.parse("text/plain"));
        RequestBody previousPrice = RequestBody.create(String.valueOf(product.getPreviousPrice()), MediaType.parse("text/plain"));
        RequestBody unit = RequestBody.create(product.getUnit(), MediaType.parse("text/plain"));
        RequestBody unitChange = RequestBody.create(String.valueOf(product.getUnitChange()), MediaType.parse("text/plain"));
        RequestBody stock = RequestBody.create(String.valueOf(product.getStock()), MediaType.parse("text/plain"));
        RequestBody categoryId = RequestBody.create(product.getCategory().getId(), MediaType.parse("text/plain"));

        MultipartBody.Part thumbnail = null;
        if(imageUri != null) {
            try {
                thumbnail = imagePickerHelper.createPartFromUri(imageUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        RetrofitClient
            .getAuthClient(getUserToken())
            .create(ProductAPI.class)
            .createProduct(
                name, description, price, previousPrice, unit, unitChange, stock, categoryId, thumbnail
            ).enqueue(new Callback<ProductResponses.SingleProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponses.SingleProductResponse> call, Response<ProductResponses.SingleProductResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(AddProductActivity.this, "Product created successfully", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("newProduct", response.body().getProduct()); // Product must be Parcelable
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        try {
                            Toast.makeText(AddProductActivity.this, "Error adding product: " +response.message(), Toast.LENGTH_SHORT).show();
                            Log.d("Add Product", "onResponse: " + response.message() + ": " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductResponses.SingleProductResponse> call, Throwable t) {
                    Log.d("Add Product", "onFailure: " + t.getMessage());
                }
            });
    }

    private void setupProductSkuSpinner() {
        BaseTypeHelper.getProductSkuList(getUserToken(), skuList -> {
            if (skuList.isEmpty()) return;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    AddProductActivity.this, android.R.layout.simple_spinner_item, skuList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            productSkuSpinner.setAdapter(adapter);
        });
    }
}