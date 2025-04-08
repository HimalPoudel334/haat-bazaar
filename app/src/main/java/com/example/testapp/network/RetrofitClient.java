package com.example.testapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "http://192.168.18.50:8080";
    public static final String CURRENT_USER_ID = "352051cc-d689-4445-9ef0-d27c6c69d3dd";  //john doe
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit != null) return retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    private static Gson gson = null;
    public static Gson getGson() {
        if(gson != null) return gson;
        gson = new GsonBuilder().setPrettyPrinting().create();
        return gson;
    }
}
