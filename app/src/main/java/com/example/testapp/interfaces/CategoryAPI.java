package com.example.testapp.interfaces;

import com.example.testapp.responses.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryAPI {
    @GET("/categories")
    Call<CategoryResponse> getCategories();

    @GET("/category/{category_id}")
    Call<CategoryResponse> getCategory(@Path("category_id") String category_id);

}
