package com.example.testapp.apis;

import com.example.testapp.requestmodels.ProductRatingRequest;
import com.example.testapp.responses.ProductRatingResponses;
import com.example.testapp.responses.ProductResponses;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductAPI {
    @GET("products")
    Call<ProductResponses.MultipleProductResonse> getProducts(@Query("categoryId") String categoryId);
    @GET("products/{productId}")
    Call<ProductResponses.SingleProductResponse> getProduct(@Path("productId") String productId);

    @Multipart
    @POST("admin/products")
    Call<ProductResponses.SingleProductResponse> createProduct(@Part("name") RequestBody name,
                                                               @Part("description") RequestBody description,
                                                               @Part("price") RequestBody price,
                                                               @Part("previousPrice") RequestBody previousPrice,
                                                               @Part("unit") RequestBody unit,
                                                               @Part("unitChange") RequestBody unitChange,
                                                               @Part("stock") RequestBody stock,
                                                               @Part("categoryId") RequestBody categoryId,
                                                               @Part MultipartBody.Part image);

    @Multipart
    @PUT("admin/products/{productId}")
    Call<ProductResponses.SingleProductResponse> updateProduct(@Path("productId") String productId,
                                                               @Part("name") RequestBody name,
                                                               @Part("description") RequestBody description,
                                                               @Part("price") RequestBody price,
                                                               @Part("previousPrice") RequestBody previousPrice,
                                                               @Part("unit") RequestBody unit,
                                                               @Part("unitChange") RequestBody unitChange,
                                                               @Part("stock") RequestBody stock,
                                                               @Part("categoryId") RequestBody categoryId,
                                                               @Part MultipartBody.Part image);


    @POST("products/{productId}/rate")
    Call<Void> rateProduct(@Path("productId") String productId, @Body ProductRatingRequest request);

    @GET("products/{productId}/ratings")
    Call<ProductRatingResponses.MultiProductRatingResponse> getProductRatings(@Path("productId") String productId);

    @GET("products/{productId}/rating/{userId}")
    Call<ProductRatingResponses.SingleProductRatingResponse> getUserProductRating(@Path("productId") String productId, @Path("userId") String userId);

}
