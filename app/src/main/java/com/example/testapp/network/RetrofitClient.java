package com.example.testapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "http://192.168.18.14:8080";
    public static final String CURRENT_USER_ID = "352051cc-d689-4445-9ef0-d27c6c69d3dd";  //john doe
    private static Retrofit publicClient = null;
    private static Retrofit authClient = null;

    // Public client (no token)
    public static Retrofit getClient() {
        if (publicClient == null) {
            publicClient = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return publicClient;
    }

    // Authenticated client (with token)
    public static Retrofit getAuthClient(String token) {
        if (authClient == null || tokenChanged(token)) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();
                        return chain.proceed(newRequest);
                    })
                    .build();

            authClient = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return authClient;
    }

    // Optional: helper to invalidate authClient if token changes
    private static String lastToken = null;
    private static boolean tokenChanged(String token) {
        boolean changed = !token.equals(lastToken);
        lastToken = token;
        return changed;
    }

    private static Gson gson = null;
    public static Gson getGson() {
        if(gson != null) return gson;
        gson = new GsonBuilder().setPrettyPrinting().create();
        return gson;
    }
}
