package com.example.testapp.interfaces;

import com.example.testapp.models.ProductImage;
import com.example.testapp.responses.ProductImageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductImagesAPI {
    @GET("/products/{productId}/images")
    Call<ProductImageResponse> getProductExtraImages(@Path("productId") String productId);

    @POST("/products/{productId}/images")
    Call<ProductImageResponse> createProductExtraImage(@Path("productId") String productId, @Body ProductImage productImage);
}
