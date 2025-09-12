package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.apis.AuthAPI;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.requestmodels.AuthRequest;
import com.example.testapp.responses.AuthResponses;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextView messageTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messageTv = findViewById(R.id.messageTv);
        EditText emailET = findViewById(R.id.emailET);
        emailET.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                messageTv.setText("");
            }
        });

        Button cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(v -> {
            finish();
        });

        Button sendBtn = findViewById(R.id.send_button);
        sendBtn.setOnClickListener(v -> {
            String email = emailET.getText().toString().trim();

            if (email.isEmpty()) {
                messageTv.setText(R.string.email_is_required);
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                messageTv.setText(R.string.invalid_email_address);
            } else {
                requestPasswordReset(email);
            }
        });
    }

    private void requestPasswordReset(String email) {
        RetrofitClient
            .getClient()
            .create(AuthAPI.class)
            .requestPasswordReset(
                    new AuthRequest.PasswordResetRequest(email))
            .enqueue(new Callback<AuthResponses.OtpResponse>() {
                @Override
                public void onResponse(Call<AuthResponses.OtpResponse> call, Response<AuthResponses.OtpResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponses.OtpResponse otpResponse = response.body();
                        Toast.makeText(ForgetPasswordActivity.this, otpResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Forget Password", "onResponse: " + otpResponse.getMessage() + " exp in: " + otpResponse.getExpiresInMinutes());

                        Intent intent = new Intent(ForgetPasswordActivity.this, VerifyOtpActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("message", otpResponse.getMessage());
                        intent.putExtra("expiresInMinutes", otpResponse.getExpiresInMinutes());
                        startActivity(intent);
                        finish();
                    }
                    else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("Forget Password", "onResponse: " + response.message() + ": " + errorBody);

                            // Parse the error JSON and extract "message"
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String errorMessage = jsonObject.optString("message", "An unexpected error occurred");

                            // Show it in your UI (TextView)
                            messageTv.setText(errorMessage);
                            messageTv.setVisibility(View.VISIBLE);

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            messageTv.setText(R.string.an_error_occurred_please_try_again);
                            messageTv.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthResponses.OtpResponse> call, Throwable t) {

                }
        });
    }
}