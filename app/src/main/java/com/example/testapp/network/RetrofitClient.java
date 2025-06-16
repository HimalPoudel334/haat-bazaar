package com.example.testapp.network;

import com.example.testapp.managers.AuthManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "http://159.13.60.202/haatbazaar-api/";
    private static volatile Retrofit publicClient = null;
    private static volatile Retrofit authClient = null;
    private static volatile String currentToken = null;

    // Public client (no token)
    public static Retrofit getClient() {
        Retrofit client = publicClient;
        if (client == null) {
            synchronized (RetrofitClient.class) {
                client = publicClient;
                if (client == null) {
                    publicClient = client = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return publicClient;
    }

    // Authenticated client (with token)
    public static Retrofit getAuthClient(String token) {
        Retrofit client = authClient;
        if (client == null || !token.equals(currentToken)) {
            synchronized (RetrofitClient.class) {
                if (authClient == null || !token.equals(currentToken)) {
                    OkHttpClient c = new OkHttpClient.Builder()
                            .addInterceptor(chain -> {
                                Request newRequest = chain.request().newBuilder()
                                        .addHeader("Authorization", "Bearer " + token)
                                        .build();
                                Response response = chain.proceed(newRequest);
                                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                    AuthManager.getInstance().logout();
                                }
                                return response;
                            })
                            .build();

                    authClient = client = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(c)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    currentToken = token;
                }
            }
        }
        return authClient;
    }

    private static Gson gson = null;
    public static Gson getGson() {
        if(gson != null) return gson;
        gson = new GsonBuilder().setPrettyPrinting().create();
        return gson;
    }
}
