package com.example.testapp.interfaces;

import com.example.testapp.authrequests.LoginRequest;
import com.example.testapp.responses.AuthResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("auth/login")
    Call<AuthResponses.LoginResponse> login(@Body LoginRequest loginRequest);
}
