package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;


import com.example.testapp.authrequests.LoginRequest;
import com.example.testapp.apis.AuthAPI;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.AuthResponses;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {

    private EditText emailET;
    private EditText passwordET;
    private TextView loginResponseTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loginResponseTv = findViewById(R.id.loginResponseTv);

        Button login = findViewById(R.id.login);
        TextView registerTv = findViewById(R.id.registerTv);

        emailET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                loginResponseTv.setVisibility(View.GONE);
            }
        });

        passwordET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                loginResponseTv.setVisibility(View.GONE);
            }
        });

        login.setOnClickListener(v -> {
            String username = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            performLogin(username, password);
        });

        registerTv.setOnClickListener(v -> {
           Intent intent = new Intent(this, RegisterActivity.class);
           startActivity(intent);
           finish();
        });
    }

    private void performLogin(String username, String password) {
        Retrofit retrofitClient = RetrofitClient.getClient();
        AuthAPI authAPI = retrofitClient.create(AuthAPI.class);
        LoginRequest request = new LoginRequest(username, password);

        authAPI.login(request).enqueue(new Callback<AuthResponses.LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponses.LoginResponse> call, @NonNull Response<AuthResponses.LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    AuthResponses.LoginResponse loginResponse = response.body();
                    if (loginResponse.getUser() != null && loginResponse.getAccessToken() != null && loginResponse.getRefreshToken() != null) {
                        AuthManager.getInstance().setFullUser(loginResponse.getUser());
                        AuthManager.getInstance().saveUserTokens(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
                        finish();
                    } else {
                        loginResponseTv.setVisibility(View.VISIBLE);
                        loginResponseTv.setText(R.string.login_failed_invalid_username_or_password);
                    }
                } else {
                    int statusCode = response.code();
                    if (statusCode == 401) {
                        loginResponseTv.setVisibility(View.VISIBLE);
                        loginResponseTv.setText(R.string.login_failed_invalid_username_or_password);
                    } else {
                        try {
                            Log.d("Login", "onResponse: "+response.errorBody().string());
                        } catch (IOException e) {
                            Log.d("Login", "onResponse: Exc "+e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponses.LoginResponse> call, @NonNull Throwable t) {

            }
        });
    }
}