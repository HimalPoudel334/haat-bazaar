package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.testapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private Product selectedProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        selectedProduct = (Product) getIntent().getExtras().getParcelable("selectedProduct");

        setupMainContent();
    }

    private void setupMainContent() {
        showSelectedProductImageSlider();

        TextView productName = (TextView) findViewById(R.id.selected_product_name);
        TextView productDescription = (TextView) findViewById(R.id.selected_product_description);
        TextView productPrice = (TextView) findViewById(R.id.selected_product_price);
        TextView productPreviousPrice = (TextView) findViewById(R.id.selected_product_previous_price);
        Button addToCart = (Button) findViewById(R.id.add_to_cart);
        Button buyNow = (Button) findViewById(R.id.buy_now);

        productName.setText(selectedProduct.getProductName());
        productDescription.setText(selectedProduct.getProductDescription());
        productPrice.setText(String.format("%s %s", selectedProduct.getProductPrice(), selectedProduct.getProductUnit()));
        productPreviousPrice.setText(String.format("%s %s", selectedProduct.getProductPrice(), selectedProduct.getProductUnit()));
        productPreviousPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        
        addToCart.setOnClickListener(view -> {
            Toast.makeText(ProductDetailActivity.this, "Add to cart button clicked", Toast.LENGTH_SHORT).show();
        });

        buyNow.setOnClickListener(view -> {
            Toast.makeText(ProductDetailActivity.this, "Buy now button clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void showSelectedProductImageSlider() {
        List<SlideModel> productImages = new ArrayList<SlideModel>();
        productImages.add(new SlideModel(selectedProduct.getProductImage(), selectedProduct.getProductName(), ScaleTypes.CENTER_CROP));

        ImageSlider imageSlider = (ImageSlider) findViewById(R.id.selected_product_images_slider);
        imageSlider.setImageList(productImages);
    }
}