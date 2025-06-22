package com.example.testapp.interfaces;

import com.example.testapp.models.Cart;
import com.example.testapp.responses.CartResponses;
import com.example.testapp.responses.ShipmentResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShipmentAPI {
    @GET("admin/shipments")
    Call<ShipmentResponses.MultiShipmentResponse> getShipments();
}
