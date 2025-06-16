package com.example.testapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.models.Category;
import com.example.testapp.models.Product;

public class AddProductActivity extends BaseActivity {

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

        setupMainContent();
    }

    private void setupMainContent() {
        findViewById(R.id.save_button).setOnClickListener(v -> {
            String name = ((EditText) findViewById(R.id.product_name_et)).getText().toString().trim();
            String description = ((EditText) findViewById(R.id.product_description_et)).getText().toString().trim();
            double price = Double.parseDouble(((EditText) findViewById(R.id.product_price_et)).getText().toString().trim());
            double previousPrice = Double.parseDouble(((EditText) findViewById(R.id.product_previous_price_et)).getText().toString().trim());
            double stock = Double.parseDouble(((EditText) findViewById(R.id.product_stock_et)).getText().toString().trim());
            String sku = ((Spinner) findViewById(R.id.product_sku_spinner)).getSelectedItem().toString().trim();
            double unitChange = Double.parseDouble(((EditText) findViewById(R.id.product_unit_change_et)).getText().toString().trim());
            Category category = (Category) ((Spinner) findViewById(R.id.category_spinner)).getSelectedItem();

            ImageView imageView = findViewById(R.id.image_view);

            Product product = new Product(name, "", description, price, previousPrice, sku, unitChange, stock, category);
        });
    }
}