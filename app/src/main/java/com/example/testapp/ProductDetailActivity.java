package com.example.testapp;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.testapp.adapters.ProductReviewListAdapter;
import com.example.testapp.apis.CartAPI;
import com.example.testapp.apis.ProductAPI;
import com.example.testapp.apis.ProductImagesAPI;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Product;
import com.example.testapp.models.ProductImage;
import com.example.testapp.models.ProductRating;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.requestmodels.ProductRatingRequest;
import com.example.testapp.responses.CartResponses;
import com.example.testapp.responses.ProductImageResponse;
import com.example.testapp.responses.ProductRatingResponses;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        Button rateBtn = findViewById(R.id.rate_btn);

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

        rateBtn.setOnClickListener(view -> {
            if(!isUserLoggedIn()) {
                Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            }

            // First fetch existing rating, then show dialog
            fetchUserRatingAndShowDialog();
        });

        showSelectedProductImageSlider();

        showProductReviews();
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

    private void showProductReviews() {
        RetrofitClient
            .getClient()
            .create(ProductAPI.class)
            .getProductRatings(selectedProduct.getId())
            .enqueue(new Callback<ProductRatingResponses.MultiProductRatingResponse>() {
                @Override
                public void onResponse(Call<ProductRatingResponses.MultiProductRatingResponse> call, Response<ProductRatingResponses.MultiProductRatingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ProductRating> ratings = response.body().getProductRatings();
                        RecyclerView productReviewsRecyclerView = findViewById(R.id.product_reviews_rv);
                        if (ratings != null && !ratings.isEmpty()) {
                            float avgRating = (float) ratings.stream()
                                    .mapToDouble(ProductRating::getRating)
                                    .average()
                                    .orElse(0.0);

                            RatingBar productRatingBar = findViewById(R.id.product_rating);
                            productRatingBar.setRating(avgRating);

                            productReviewsRecyclerView.setAdapter(new ProductReviewListAdapter(ratings));
                            productReviewsRecyclerView.setHasFixedSize(true);
                            productReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        } else {
                            TextView noReviewsTv = findViewById(R.id.no_reviews_tv);
                            TextView reviewTv = findViewById(R.id.review_tv);
                            noReviewsTv.setVisibility(View.VISIBLE);
                            productReviewsRecyclerView.setVisibility(View.GONE);
                            reviewTv.setVisibility(View.GONE);
                            Log.d("ProductDetails", "No product ratings available.");
                        }
                    }
                    else {
                        try {
                            if (response.errorBody() != null) {
                                String errorJson = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Fetching review failed.");

                                Toast.makeText(ProductDetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                Log.e("Verify Otp", "Error response: " + errorMessage);
                            } else {
                                Toast.makeText(ProductDetailActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductDetailActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductRatingResponses.MultiProductRatingResponse> call, Throwable t) {

                }
        });
    }

    private void fetchUserRatingAndShowDialog() {
        RetrofitClient
                .getAuthClient(getUserToken())
                .create(ProductAPI.class)
                .getUserProductRating(selectedProduct.getId(), getCurrentUserId())
                .enqueue(new Callback<ProductRatingResponses.SingleProductRatingResponse>() {
                    @Override
                    public void onResponse(Call<ProductRatingResponses.SingleProductRatingResponse> call, Response<ProductRatingResponses.SingleProductRatingResponse> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            // User has existing rating
                            ProductRating existingRating = response.body().getProductRating();
                            showRatingDialog(existingRating.getRating(), existingRating.getReview());
                        } else {
                            // No existing rating or error - show empty dialog
                            showRatingDialog(0.0, "");
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductRatingResponses.SingleProductRatingResponse> call, Throwable t) {
                        // Network error - show empty dialog
                        showRatingDialog(0.0, "");
                    }
                });
    }

    private void showRatingDialog(double existingRating, String existingReview) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_rate_review, null);

        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        EditText reviewInput = dialogView.findViewById(R.id.review_input);

        // Set existing values if any
        if(existingRating > 0.0) {
            ratingBar.setRating((float) existingRating);
        }
        if(!existingReview.isEmpty()) {
            reviewInput.setText(existingReview);
        }

        // Build the dialog
        new AlertDialog.Builder(this)
                .setTitle(existingRating > 0 ? "Update Rating & Review" : "Rate & Review")
                .setView(dialogView)
                .setPositiveButton(existingRating > 0 ? "Update" : "Submit", (dialog, which) -> {
                    float rating = ratingBar.getRating();
                    String review = reviewInput.getText().toString().trim();

                    if(rating == 0) {
                        Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(review.isEmpty()) {
                        Toast.makeText(this, "Please enter a review", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    rateProduct((int) rating, review);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void rateProduct(int rating, String review) {
        ProductRatingRequest req = new ProductRatingRequest(rating, review, getCurrentUserId());
        Log.d("Rate Product", "rateProduct: " +     RetrofitClient.getGson().toJson(req));
        RetrofitClient
            .getAuthClient(getUserToken())
            .create(ProductAPI.class)
            .rateProduct(
                selectedProduct.getId(),
                req
            )
            .enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(ProductDetailActivity.this, "Rating submitted", Toast.LENGTH_SHORT).show();
                        showProductReviews();
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                String errorJson = response.errorBody().string();
                                Log.e("Verify Otp", "Error response: " + errorJson);
                                Toast.makeText(ProductDetailActivity.this, errorJson, Toast.LENGTH_LONG).show();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Submitting review failed.");

                            } else {
                                Toast.makeText(ProductDetailActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductDetailActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
    }
}