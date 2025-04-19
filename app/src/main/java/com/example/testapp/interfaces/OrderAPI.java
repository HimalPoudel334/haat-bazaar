package com.example.testapp.interfaces;

import com.example.testapp.models.Order;
import com.example.testapp.responses.OrderResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderAPI {
    @POST("/orders")
    Call<OrderResponses.SingleOrderResponse> createOrder(@Body Order order);

    @GET("/orders/{orderId}")
    Call<OrderResponses.SingleOrderResponse> getOrder(@Path("orderId") String orderId);

    @GET("/orders/user/{userId}")
    Call<OrderResponses.MultiOrderResponses> getOrders(@Path("userId") String userId);
}
