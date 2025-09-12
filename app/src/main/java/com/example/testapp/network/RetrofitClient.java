package com.example.testapp.network;

import android.util.Log;

import com.example.testapp.apis.AuthAPI;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.responses.AuthResponses;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "https://himalpoudel.name.np/haatbazaar-api/";
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
        // Check if token is null or empty, or if client needs to be recreated
        if (client == null || token == null || token.isEmpty() || !token.equals(currentToken)) {
            synchronized (RetrofitClient.class) {
                if (authClient == null || token == null || token.isEmpty() || !token.equals(currentToken)) {
                    // Handle null or empty token
                    if (token == null || token.isEmpty()) {
                        Log.e("AuthInterceptor", "Access token is null or empty, logging out");
                        AuthManager.getInstance().logout();
                        return null; // Caller must handle null
                    }

                    OkHttpClient c = new OkHttpClient.Builder()
                            .addInterceptor(chain -> {
                                Request originalRequest = chain.request();
                                Request newRequest = originalRequest.newBuilder()
                                        .addHeader("Authorization", "Bearer " + token)
                                        .build();

                                Log.d("AuthInterceptor", "Sending request with token: " + token);

                                okhttp3.Response response = null; // Declare outside try-with-resources
                                try {
                                    response = chain.proceed(newRequest);

                                    // Handle 401 Unauthorized
                                    if (response.code() == 401) {
                                        Log.d("AuthInterceptor", "Received 401 Unauthorized, checking retry: " + originalRequest.header("X-Retry"));
                                        if (!"true".equals(originalRequest.header("X-Retry"))) {
                                            String refreshToken = AuthManager.getInstance().getRefreshToken();
                                            if (refreshToken == null || refreshToken.isEmpty()) {
                                                Log.e("AuthInterceptor", "Refresh token is null or empty, logging out");
                                                AuthManager.getInstance().logout();

                                                return response;
                                            }

                                            AuthResponses.RefreshResponse refreshRequest = new AuthResponses.RefreshResponse();
                                            refreshRequest.setAccessToken(token);
                                            refreshRequest.setRefreshToken(refreshToken);

                                            Retrofit refreshClient = new Retrofit.Builder()
                                                    .baseUrl(BASE_URL)
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            try {
                                                Log.d("AuthInterceptor", "Attempting token refresh with refresh token: " + refreshToken);
                                                Call<AuthResponses.RefreshResponse> call = refreshClient.create(AuthAPI.class)
                                                        .refreshToken(refreshRequest);

                                                Response<AuthResponses.RefreshResponse> refreshResponse = call.execute(); // Retrofit Response

                                                // We need to consume the error body for logging, but only once.
                                                // Store it if you need to log it.
                                                String errorBodyString = null;
                                                if (!refreshResponse.isSuccessful() && refreshResponse.errorBody() != null) {
                                                    try {
                                                        errorBodyString = refreshResponse.errorBody().string();
                                                    } catch (IOException e) {
                                                        Log.e("AuthInterceptor", "Error reading refresh error body: " + e.getMessage());
                                                    }
                                                }

                                                if (refreshResponse.isSuccessful() && refreshResponse.body() != null && refreshResponse.body().getAccessToken() != null) {
                                                    String newAccessToken = refreshResponse.body().getAccessToken();
                                                    String newRefreshToken = refreshResponse.body().getRefreshToken();
                                                    Log.d("AuthInterceptor", "Token refresh successful, new access token: " + newAccessToken);

                                                    AuthManager.getInstance().saveUserTokens(newAccessToken, newRefreshToken);

                                                    // Close the initial 401 response as we are replacing it with a new request
                                                    response.close(); // Close the initial 401 response

                                                    Request retryRequest = originalRequest.newBuilder()
                                                            .removeHeader("Authorization")
                                                            .addHeader("Authorization", "Bearer " + newAccessToken)
                                                            .header("X-Retry", "true")
                                                            .build();

                                                    Log.d("AuthInterceptor", "Retrying request with new token: " + newAccessToken);
                                                    return chain.proceed(retryRequest); // Return new response
                                                } else {
                                                    Log.e("AuthInterceptor", "Token refresh failed, status: " + refreshResponse.code() + ", body: " + (errorBodyString != null ? errorBodyString : "null"));
                                                    AuthManager.getInstance().logout();
                                                    // Close the initial 401 response as we are logging out and returning it directly.
                                                    return response; // Return original response (now closed in the calling code)
                                                }
                                            } catch (IOException e) {
                                                Log.e("AuthInterceptor", "Token refresh failed with exception: " + e.getMessage());
                                                e.printStackTrace();
                                                AuthManager.getInstance().logout();
                                                // Close the initial 401 response as we are logging out and returning it directly.
                                                return response; // Return original response (now closed in the calling code)
                                            }
                                        } else {
                                            Log.d("AuthInterceptor", "Retry already attempted, logging out");
                                            AuthManager.getInstance().logout();
                                            // Close the initial 401 response as we are logging out and returning it directly.
                                            return response; // Return original response (now closed in the calling code)
                                        }
                                    }

                                    Log.d("AuthInterceptor", "Request successful, status: " + response.code());
                                    return response; // Return original response
                                } finally {
                                    // Important: Only close the 'response' here if it's NOT being returned.
                                    // If `response` is returned from the interceptor, OkHttp's dispatcher
                                    // is responsible for closing it when its body is consumed.
                                    // Your original `try (okhttp3.Response response = chain.proceed(newRequest))`
                                    // structure automatically closes the response when the try block exits,
                                    // which is problematic if you then try to return `response`!
                                    // The `try-with-resources` should not be used if you intend to return the response.
                                }
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
