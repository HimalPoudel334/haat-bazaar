package com.example.testapp.interfaces;

import com.example.testapp.responses.CustomerResponses;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CustomerAPI {
    @GET("/customers")
    Call<CustomerResponses.MultiCustomerResponse> getCustomers();

    @GET("/customers/{customerId}")
    Call<CustomerResponses.SingleCustomerResponse> getCustomer(@Path("customerId") String customerId);

}
