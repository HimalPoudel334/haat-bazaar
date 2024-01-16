package com.example.testapp.interfaces;

import com.example.testapp.models.Product;
import com.example.testapp.responses.ProductResponses;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductAPI {
    @GET("/products")
    Call<ProductResponses.MultipleProductResonse> getProducts();

    @GET("/products/{productId}")
    Call<ProductResponses.SingleProductResponse> getProduct(@Path("productId") String productId);

    @POST("/products")
    Call<ProductResponses.SingleProductResponse> createProduct(@Body Product product);
}
