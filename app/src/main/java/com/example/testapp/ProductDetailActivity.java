package com.example.testapp;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.testapp.apis.CartAPI;
import com.example.testapp.apis.ProductImagesAPI;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Product;
import com.example.testapp.models.ProductImage;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.CartResponses;
import com.example.testapp.responses.ProductImageResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductDetailActivity extends BaseActivity {

    private Product selectedProduct;
    private ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        selectedProduct = (Product) getIntent().getExtras().getParcelable("selectedProduct");

        //setup toolbar
        activateToolbar(true, selectedProduct.getName());

        imageSlider = (ImageSlider) findViewById(R.id.selected_product_images_slider);

        setupMainContent();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.menu_item_search_view);
        return true;
    }

    private void setupMainContent() {

        TextView productName = findViewById(R.id.selected_product_name);
        TextView productDescription = findViewById(R.id.selected_product_description);
        TextView productPrice = findViewById(R.id.selected_product_price);
        TextView productPreviousPrice = findViewById(R.id.selected_product_previous_price);
        Button addToCart = findViewById(R.id.add_to_cart);
        Button buyNow = findViewById(R.id.buy_now);

        productName.setText(selectedProduct.getName());
        productDescription.setText(selectedProduct.getDescription());
        productPrice.setText(String.format("Rs %s per %s", selectedProduct.getPrice(), selectedProduct.getUnit()));
        productPreviousPrice.setText(String.format("Rs %s per %s", selectedProduct.getPreviousPrice(), selectedProduct.getUnit()));
        productPreviousPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        addToCart.setOnClickListener(view -> addToCart());

        buyNow.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailActivity.this, BuyProductActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("productAboutToBuy", selectedProduct);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        showSelectedProductImageSlider();
    }

    private void showSelectedProductImageSlider() {
        List<SlideModel> productImagesModel= new ArrayList<>();
        productImagesModel.add(new SlideModel(selectedProduct.getImage(), ScaleTypes.CENTER_CROP));

        Retrofit retrofit = RetrofitClient.getClient();
        ProductImagesAPI productImagesAPI = retrofit.create(ProductImagesAPI.class);
        productImagesAPI.getProductExtraImages(selectedProduct.getId()).enqueue(new Callback<ProductImageResponse>() {
            @Override
            public void onResponse(Call<ProductImageResponse> call, Response<ProductImageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ProductImage img : response.body().getProductExtraImages()) {
                        String imageUrl = img.getImageName();
                        Log.d("product extra image URL", imageUrl); // Add this line
                        productImagesModel.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                    }
                }
                imageSlider.setImageList(productImagesModel);
            }

            @Override
            public void onFailure(Call<ProductImageResponse> call, Throwable t) {
                Log.d("product extra", "onFailure: "+t.getMessage());
            }
        });
    }


    private void addToCart() {
        double quantity = 1.0;
        Date cartCreatedDate = GregorianCalendar.getInstance().getTime();
        String createdOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(cartCreatedDate);

        Cart cart = new Cart(selectedProduct.getId(), quantity, createdOn);

        CartAPI cartAPI = RetrofitClient.getAuthClient(getUserToken()).create(CartAPI.class);
        cartAPI.createCart(getCurrentUserId(), cart).enqueue(new Callback<CartResponses.SingleCartResponse>() {
            @Override
            public void onResponse(Call<CartResponses.SingleCartResponse> call, Response<CartResponses.SingleCartResponse> response) {
                if(response.isSuccessful())
                    Toast.makeText(getApplicationContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
                Log.d("Create cart", "onResponse: (try) "+response.message());
            }

            @Override
            public void onFailure(Call<CartResponses.SingleCartResponse> call, Throwable t) {
                Log.d("Create Order", "onFailure: " + t.getMessage());
            }
        });
    }
}