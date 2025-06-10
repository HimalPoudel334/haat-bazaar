package com.example.testapp.interfaces;

import com.example.testapp.models.Cart;
import com.example.testapp.models.Order;
import com.example.testapp.responses.CartResponses;
import com.example.testapp.responses.CategoryResponses;
import com.example.testapp.responses.OrderResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartAPI {
    @GET("carts")
    Call<CartResponses.MultiCartResponse> getCarts();

    @GET("carts/{UserId}")
    Call<CartResponses.MultiCartResponse> getUserCart(@Path("UserId") String UserId);

    @POST("carts/{UserId}")
    Call<CartResponses.SingleCartResponse> createCart(@Path("UserId") String UserId, @Body Cart cart);

    @DELETE("carts/{cartId}")
    Call<Void> deleteCart(@Path("cartId") String cartId);

    @DELETE("carts/delete-carts/{UserId}")
    Call<Void> deleteUserCart(@Path("UserId") String UserId);
}
