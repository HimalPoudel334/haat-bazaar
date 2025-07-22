package com.example.testapp.apis;

import com.example.testapp.requestmodels.FcmRegistrationTokenRequest;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FcmRegistrationTokenAPI {
    @POST("admin/device/register-fcm-token")
    Call<Void> sendRegistrationToken(@Body FcmRegistrationTokenRequest loginRequest);
}
