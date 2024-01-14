package com.example.testapp.interfaces;

import com.example.testapp.models.Product;
import com.example.testapp.responses.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductAPI {
    @GET("/products")
    Call<ProductResponse> getProducts();

    @GET("/products/{product_id}")
    Call<ProductResponse> getProduct(@Path("product_id") String productId);

    @POST("/products")
    Call<ProductResponse> createProduct(@Body Product product);
}
