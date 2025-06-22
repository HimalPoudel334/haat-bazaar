package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.ProductListAdapter;
import com.example.testapp.interfaces.ProductAPI;
import com.example.testapp.models.Product;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.ProductResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends BaseActivity {

    private ActivityResultLauncher<Intent> addProductLauncher;
    private ActivityResultLauncher<Intent> editProductLauncher;
    private RecyclerView productRecyclerView;
    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Products");

        productRecyclerView = findViewById(R.id.product_list_rv);

        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Product newProduct = result.getData().getParcelableExtra("newProduct");
                        if (newProduct != null && adapter != null) {
                            adapter.addProduct(newProduct);
                        }
                    }
                }
        );

        editProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Product updatedProduct = result.getData().getParcelableExtra("updatedProduct");
                        if (updatedProduct != null && adapter != null) {
                            adapter.updateProduct(updatedProduct);
                        }
                    }
                }
        );


        Button addNew = findViewById(R.id.addNew);
        addNew.setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
            addProductLauncher.launch(intent);
        });

        setupMainContent();
    }

    private void setupMainContent() {


        RetrofitClient
            .getAuthClient(getUserToken())
            .create(ProductAPI.class)
            .getProducts()
            .enqueue(new Callback<ProductResponses.MultipleProductResonse>() {
                @Override
                public void onResponse(Call<ProductResponses.MultipleProductResonse> call, Response<ProductResponses.MultipleProductResonse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        adapter = new ProductListAdapter(ProductActivity.this, response.body().getProducts());
                        RecyclerView crv = findViewById(R.id.product_list_rv);
                        crv.setLayoutManager(new GridLayoutManager(ProductActivity.this, 2));
                        crv.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<ProductResponses.MultipleProductResonse> call, Throwable t) {

                }
            });

    }

    public void launchEditProduct(Product product) {
        Intent intent = new Intent(this, EditProductActivity.class);
        intent.putExtra("selectedProduct", product);
        editProductLauncher.launch(intent);
    }
}