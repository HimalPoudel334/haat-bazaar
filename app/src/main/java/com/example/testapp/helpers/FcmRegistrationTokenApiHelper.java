package com.example.testapp.helpers;

import android.util.Log;

import com.example.testapp.apis.FcmRegistrationTokenAPI;
import com.example.testapp.requestmodels.FcmRegistrationTokenRequest;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FcmRegistrationTokenApiHelper {
    private static final String TAG = "FcmTokenApiHelper";

    /**
     * Sends the FCM registration token to backend.
     */
    public void sendRegistrationTokenToBackend(String adminId, String fcmToken) {

        RetrofitClient.getAuthClient(AuthManager.getInstance().getAccessToken())
                .create(FcmRegistrationTokenAPI.class)
                .sendRegistrationToken(new FcmRegistrationTokenRequest(adminId, fcmToken))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: token send successfully");
                        } else {
                            try {
                                Log.d(TAG, "onResponse: something went wrong during reg with backend: " + response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Error while sending token to backend", t);
                    }
                });
    }

}
