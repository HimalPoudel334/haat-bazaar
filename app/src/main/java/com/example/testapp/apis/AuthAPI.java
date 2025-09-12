package com.example.testapp.apis;

import com.example.testapp.requestmodels.AuthRequest;
import com.example.testapp.responses.AuthResponses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("auth/login")
    Call<AuthResponses.LoginResponse> login(@Body AuthRequest.LoginRequest loginRequest);

    @POST("auth/refresh")
    Call<AuthResponses.RefreshResponse> refreshToken(@Body AuthResponses.RefreshResponse refreshRequest);

    @POST("auth/password-reset/request")
    Call<AuthResponses.OtpResponse> requestPasswordReset(@Body AuthRequest.PasswordResetRequest request);

    @POST("auth/password-reset/verify-otp")
    Call<AuthResponses.VerifyOtpResponse> verifyOtp(@Body AuthRequest.VerifyOtpRequest request);

    @POST("auth/password-reset/new-password")
    Call<AuthResponses.VerifyOtpResponse> newPassword(@Body AuthRequest.NewPasswordRequest request);
}
