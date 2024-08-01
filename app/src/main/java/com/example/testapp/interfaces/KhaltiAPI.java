package com.example.testapp.interfaces;

import com.example.testapp.responses.CustomerResponses;
import com.example.testapp.responses.KhaltiResponses;

import retrofit2.Call;
import retrofit2.http.GET;

public interface KhaltiAPI {
    @GET("/payments/khalti/payment")
    Call<KhaltiResponses.KhaltiPidxResponse> getPidx();
}
