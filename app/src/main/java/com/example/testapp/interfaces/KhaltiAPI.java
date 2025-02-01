package com.example.testapp.interfaces;

import com.example.testapp.models.Order;
import com.example.testapp.responses.KhaltiResponses;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface KhaltiAPI {
    @POST("/payments/khalti")
    Call<JsonElement> getKhaltiPayload(@Body Order order);

    @POST("https://dev.khalti.com/api/v2/epayment/initiate/")
    Call<JsonElement> initiatePayment(
            @Header("Authorization") String authorizationHeader,
            @Header("Content-Type") String contentType,
            @Body JsonElement payload
    );
}
