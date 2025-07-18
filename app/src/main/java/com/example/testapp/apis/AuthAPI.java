package com.example.testapp.apis;

import com.example.testapp.authrequests.LoginRequest;
import com.example.testapp.responses.AuthResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("auth/login")
    Call<AuthResponses.LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/refresh")
    Call<AuthResponses.RefreshResponse> refreshToken(@Body AuthResponses.RefreshResponse refreshRequest);
}
