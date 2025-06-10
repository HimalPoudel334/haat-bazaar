package com.example.testapp.interfaces;

import com.example.testapp.models.KhaltiPayment;
import com.example.testapp.models.Order;
import com.example.testapp.responses.KhaltiResponses;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KhaltiAPI {
    @GET("payments/khalti")
    Call<JsonElement> getKhaltiPidx(@Query("orderId") String orderId);

    @POST("https://dev.khalti.com/api/v2/epayment/initiate/")
    Call<JsonElement> initiatePayment(
            @Header("Authorization") String authorizationHeader,
            @Header("Content-Type") String contentType,
            @Body JsonElement payload
    );

    @POST("payments/khalti/confirmation")
    Call<JsonElement> verifyPayment(@Body KhaltiPayment.KhaltiPaymentConfirmPayload payload);
}
