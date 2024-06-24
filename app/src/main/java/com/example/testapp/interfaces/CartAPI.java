package com.example.testapp.interfaces;

import com.example.testapp.models.Cart;
import com.example.testapp.models.Order;
import com.example.testapp.responses.CartResponses;
import com.example.testapp.responses.CategoryResponses;
import com.example.testapp.responses.OrderResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartAPI {
    @GET("/carts")
    Call<CartResponses.MultiCartResponse> getCarts();

    @GET("/carts/{customerId}")
    Call<CartResponses.SingleCartResponse> getCart(@Path("customerId") String customerId);

    @POST("/carts/{customerId}")
    Call<CartResponses.SingleCartResponse> createCart(@Path("customerId") String customerId, @Body Cart cart);
}
