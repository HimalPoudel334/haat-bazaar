package com.example.testapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
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
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends BaseActivity {

    private Product product;

    private EditText productNameEt, productDescriptionEt, productPriceEt, productPreviousPriceEt, productUnitChangeEt, productStockEt;
    private Spinner productSKUSpinner, categorySpinner;
    private Button saveButton, multiImageSelectButton;
    private ImageView productImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri imageUri;
    private ImagePickerHelper imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        product = (Product) getIntent().getExtras().getParcelable("selectedProduct");
        Log.d("Product Edit", "onCreate: " + RetrofitClient.getGson().toJson(product));

        //setup toolbar
        activateToolbar(true, "Edit Product");

        imagePicker = new ImagePickerHelper(this);

        productImage = findViewById(R.id.image_view);
        Glide
            .with(this)
            .load(product.getImage())
            .signature(new ObjectKey(System.currentTimeMillis())) // unique key to break cache
            .centerCrop()
            .into(productImage);

        productImage.setOnLongClickListener(v -> {
            imagePicker.pickSingleImage((uri) -> {
                imageUri = uri;
                productImage.setImageURI(uri);
            });
            return true;
        });


        categorySpinner = findViewById(R.id.category_spinner);
        productNameEt = findViewById(R.id.product_name_et);
        productNameEt.setText(product.getName());

        productDescriptionEt = findViewById(R.id.product_description_et);
        productDescriptionEt.setText(product.getDescription());

        productPriceEt = findViewById(R.id.product_price_et);
        productPriceEt.setText(String.format(Locale.ENGLISH, "Rs %.2f", product.getPrice()));

        productPreviousPriceEt = findViewById(R.id.product_previous_price_et);
        productPreviousPriceEt.setText(String.format(Locale.ENGLISH, "Rs %.2f", product.getPreviousPrice()));

        productSKUSpinner = findViewById(R.id.product_sku_spinner);


        productUnitChangeEt = findViewById(R.id.product_unit_change_et);
        productUnitChangeEt.setText(String.format(Locale.ENGLISH, "%.2f", product.getUnitChange()));

        productStockEt = findViewById(R.id.product_stock_et);
        productStockEt.setText(String.format(Locale.ENGLISH, "%.2f", product.getStock()));

        saveButton = findViewById(R.id.save_button);
        multiImageSelectButton = findViewById(R.id.select_images_button);

        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                        productImage.setImageURI(imageUri);
                    }
                }
            }
        );

        saveButton.setOnClickListener(v -> {
            product.setName(productNameEt.getText().toString().trim());
            product.setDescription(productDescriptionEt.getText().toString().trim());
            product.setPrice(Double.parseDouble(productPriceEt.getText().toString().trim().split(" ")[1]));
            product.setPreviousPrice(Double.parseDouble(productPreviousPriceEt.getText().toString().trim().split(" ")[1]));
            product.setStock(Double.parseDouble(productStockEt.getText().toString().trim()));
            product.setUnitChange(Double.parseDouble(productUnitChangeEt.getText().toString().trim()));

            Category selectedCategory = (Category) categorySpinner.getSelectedItem();
            product.setCategory(selectedCategory);
            product.setUnit(productSKUSpinner.getSelectedItem().toString());

            updateProduct();

        });

        setupProductSkuSpinner();

        setupCategoriesDropdown();

    }

    private void updateProduct() {

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
                thumbnail = imagePicker.createPartFromUri(imageUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        RetrofitClient
            .getAuthClient(getUserToken())
            .create(ProductAPI.class)
            .updateProduct(
                product.getId(),
                name, description, price, previousPrice, unit, unitChange, stock, categoryId, thumbnail
            ).enqueue(new Callback<ProductResponses.SingleProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponses.SingleProductResponse> call, Response<ProductResponses.SingleProductResponse> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        Log.d("ProductEdit", "onResponse: " + RetrofitClient.getGson().toJson(response.body().getProduct()));
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedProduct", response.body().getProduct()); // updatedProduct is the modified object
                        setResult(RESULT_OK, resultIntent);
                        finish();
                        Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            Log.d("Product Edit", "onResponse: " + response.message() + ": " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductResponses.SingleProductResponse> call, Throwable t) {
                    Log.d("Product Edit", "onFailure: " + t.getMessage());
                }
        });
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
                                EditProductActivity.this, android.R.layout.simple_spinner_item, response.body().getCategories()
                        );
                        categorySpinner.setAdapter(adapter);
                        categorySpinner
                            .setSelection(adapter.getPosition(new Category(product.getCategory().getId(), product.getCategory().getName()))); // category comparision is done using id.
                    }
                }

                @Override
                public void onFailure(Call<CategoryResponses.MultiCategoryResponse> call, Throwable t) {

                }
        });
    }

    private void setupProductSkuSpinner() {
        BaseTypeHelper.getProductSkuList(getUserToken(), skuList -> {
            if (skuList.isEmpty()) return;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    EditProductActivity.this, android.R.layout.simple_spinner_item, skuList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            productSKUSpinner.setAdapter(adapter);
            productSKUSpinner.setSelection(adapter.getPosition(product.getUnit()));
        });
    }

}
