package com.example.testapp.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BaseTypesAPI {
    @GET("base-types/delivery-status")
    Call<List<String>> getDeliveryStatus();

    @GET("base-types/order-status")
    Call<List<String>> getOrderStatus();

    @GET("base-types/shipment-status")
    Call<List<String>> getShipmentStatus();

    @GET("base-types/payment-method")
    Call<List<String>> getPaymentMethods();

    @GET("base-types/product-sku")
    Call<List<String>> getProductUnit();
}
