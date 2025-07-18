package com.example.testapp.apis;

import com.example.testapp.responses.UserResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAPI {
    @GET("admin/users")
    Call<UserResponses.MultiUserResponse> getUsers();

    @GET("users/{userId}")
    Call<UserResponses.SingleUserResponse> getUser(@Path("userId") String userId);

}
