package com.example.testapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.testapp.interfaces.CategoryAPI;
import com.example.testapp.interfaces.ProductAPI;
import com.example.testapp.models.Category;
import com.example.testapp.models.Product;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.CategoryResponses;
import com.example.testapp.responses.ProductResponses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        productImage = findViewById(R.id.image_view);
        Glide
            .with(this)
            .load(product.getImage())
            .signature(new ObjectKey(System.currentTimeMillis())) // unique key to break cache
            .centerCrop()
            .into(productImage);

        productImage.setOnLongClickListener(v -> {
            requestImagePermission();
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
        String[] units = {"Piece", "Gram", "Man", "Quintal", "kg", "Litre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        productSKUSpinner.setAdapter(adapter);
        productSKUSpinner.setSelection(adapter.getPosition(product.getUnit()));

        productUnitChangeEt = findViewById(R.id.product_unit_change_et);
        productUnitChangeEt.setText(String.format(Locale.ENGLISH, "Rs %.2f", product.getUnitChange()));

        productStockEt = findViewById(R.id.product_stock_et);
        productStockEt.setText(String.format(Locale.ENGLISH, "Rs %.2f", product.getStock()));

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
            product.setStock(Double.parseDouble(productStockEt.getText().toString().trim().split(" ")[1]));
            product.setUnitChange(Double.parseDouble(productUnitChangeEt.getText().toString().trim().split(" ")[1]));

            Category selectedCategory = (Category) categorySpinner.getSelectedItem();
            product.setCategory(selectedCategory);
            product.setUnit(productSKUSpinner.getSelectedItem().toString());

            //call update api
            updateProduct();

        });

        setupCategoriesDropdown();

    }

    private void updateProduct() {

        File file;
        try {
            file = getFileFromUri(imageUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RequestBody name = RequestBody.create(product.getName(), MediaType.parse("text/plain"));
        RequestBody description = RequestBody.create(product.getDescription(), MediaType.parse("text/plain"));
        RequestBody price = RequestBody.create(String.valueOf(product.getPrice()), MediaType.parse("text/plain"));
        RequestBody previousPrice = RequestBody.create(String.valueOf(product.getPreviousPrice()), MediaType.parse("text/plain"));
        RequestBody unit = RequestBody.create(product.getUnit(), MediaType.parse("text/plain"));
        RequestBody unitChange = RequestBody.create(String.valueOf(product.getUnitChange()), MediaType.parse("text/plain"));
        RequestBody stock = RequestBody.create(String.valueOf(product.getStock()), MediaType.parse("text/plain"));
        RequestBody categoryId = RequestBody.create(product.getCategory().getId(), MediaType.parse("text/plain"));
        RequestBody imageRequestBody = RequestBody.create(
            file,
            MediaType.parse("image/*")
        );

        MultipartBody.Part thumbnail = MultipartBody.Part.createFormData(
            "image", // must match field name in backend
            file.getName(),
            imageRequestBody
        );

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
                        categorySpinner.setSelection(adapter.getPosition(new Category(product.getCategory().getId(), product.getCategory().getName()))); // category comparision is done using id.
                    }
                }

                @Override
                public void onFailure(Call<CategoryResponses.MultiCategoryResponse> call, Throwable t) {

                }
        });
    }

    private void requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ needs READ_MEDIA_IMAGES
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            } else {
                openImagePicker();
            }
        } else {
            // Android 12 and below
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            } else {
                openImagePicker();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImagePicker(); // now safe to launch picker
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        imagePickerLauncher.launch(intent);
    }

    private File getFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), "upload_temp.jpg");

        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        if (inputStream != null) {
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }
        outputStream.close();
        if (inputStream != null) {
            inputStream.close();
        }
        return file;
    }



}
