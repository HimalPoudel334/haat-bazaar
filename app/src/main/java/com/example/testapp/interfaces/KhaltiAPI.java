package com.example.testapp.interfaces;

import com.example.testapp.responses.KhaltiResponses;

import retrofit2.Call;
import retrofit2.http.POST;

public interface KhaltiAPI {
    @POST("/payments/khalti/payment")
    Call<KhaltiResponses.KhaltiPidxResponse> getPidx();
}
