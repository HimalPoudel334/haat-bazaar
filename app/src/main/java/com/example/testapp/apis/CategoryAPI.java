package com.example.testapp.apis;

import com.example.testapp.models.Category;
import com.example.testapp.responses.CategoryResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryAPI {
    @POST("admin/categories")
    Call<CategoryResponses.SingleCategoryResponse> createCategory(@Body Category.CategoryCreate category);


    @GET("categories")
    Call<CategoryResponses.MultiCategoryResponse> getCategories();

    @GET("category/{categoryId}")
    Call<CategoryResponses.SingleCategoryResponse> getCategory(@Path("categoryId") String categoryId);

    @PUT("admin/categories/{categoryId}")
    Call<CategoryResponses.SingleCategoryResponse> updateCategory(@Path("categoryId") String categoryId, @Body Category.CategoryCreate category);

}
