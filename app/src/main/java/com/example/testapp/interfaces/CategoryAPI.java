package com.example.testapp.interfaces;

import com.example.testapp.responses.CategoryResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryAPI {
    @GET("categories")
    Call<CategoryResponses.MultiCategoryResponse> getCategories();

    @GET("category/{categoryId}")
    Call<CategoryResponses.SingleCategoryResponse> getCategory(@Path("categoryId") String categoryId);

}
