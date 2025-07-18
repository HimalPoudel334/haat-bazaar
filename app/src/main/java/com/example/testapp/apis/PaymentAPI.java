package com.example.testapp.apis;

import com.example.testapp.responses.PaymentResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PaymentAPI {
    @GET("admin/payments")
    Call<PaymentResponses.MultiPaymentResponse> getAllPayments(@Query("initDate") String initDate, @Query("finalDate") String finalDate);

    @PUT("admin/payments/{paymentId}/complete")
    Call<PaymentResponses.MultiPaymentResponse> completePayment(@Path("paymentId") String paymentId);
}
