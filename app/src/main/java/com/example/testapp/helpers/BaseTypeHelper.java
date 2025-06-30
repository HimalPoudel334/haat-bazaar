package com.example.testapp.helpers;

import android.util.Log;

import com.example.testapp.interfaces.BaseTypesAPI;
import com.example.testapp.network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseTypeHelper {
    public static void getProductSkuList(String userToken, BaseTypeCallback callback) {
        RetrofitClient
            .getAuthClient(userToken)
            .create(BaseTypesAPI.class)
            .getProductUnit()
            .enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onResult(response.body());
                    } else {
                        callback.onResult(Collections.emptyList());
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    callback.onResult(Collections.emptyList());
                }
            });
    }

    public static void getDeliveryStatus(String userToken, BaseTypeCallback callback) {
        RetrofitClient
            .getAuthClient(userToken)
            .create(BaseTypesAPI.class)
            .getDeliveryStatus()
            .enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onResult(response.body());
                    } else {
                        callback.onResult(Collections.emptyList());
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    callback.onResult(Collections.emptyList());
                }
            });
    }

    public static void getOrderStatus(String userToken, BaseTypeCallback callback) {
        RetrofitClient
            .getAuthClient(userToken)
            .create(BaseTypesAPI.class)
            .getOrderStatus()
            .enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onResult(response.body());
                    } else {
                        callback.onResult(Collections.emptyList());
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    callback.onResult(Collections.emptyList());
                }
            });
    }

    public static void getShipmentStatus(String userToken, BaseTypeCallback callback) {
        RetrofitClient
            .getAuthClient(userToken)
            .create(BaseTypesAPI.class)
            .getShipmentStatus()
            .enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onResult(response.body());
                    } else {
                        callback.onResult(Collections.emptyList());
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    callback.onResult(Collections.emptyList());
                }
            });
    }

    public static void getPaymentStatus(String userToken, BaseTypeCallback callback) {
        RetrofitClient
                .getAuthClient(userToken)
                .create(BaseTypesAPI.class)
                .getPaymentStatus()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onResult(response.body());
                        } else {
                            callback.onResult(Collections.emptyList());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        callback.onResult(Collections.emptyList());
                    }
                });
    }

    public static void getPaymentMethods(String userToken, BaseTypeCallback callback) {
        RetrofitClient
            .getAuthClient(userToken)
            .create(BaseTypesAPI.class)
            .getPaymentMethods()
            .enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        callback.onResult(response.body());
                    } else {
                        callback.onResult(Collections.emptyList());
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    callback.onResult(Collections.emptyList());
                }
            });
    }
}
