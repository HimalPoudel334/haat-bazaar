package com.example.testapp.apis;

import com.example.testapp.responses.ShipmentResponses;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ShipmentAPI {
    @GET("admin/shipments")
    Call<ShipmentResponses.MultiShipmentResponse> getShipments();
}
