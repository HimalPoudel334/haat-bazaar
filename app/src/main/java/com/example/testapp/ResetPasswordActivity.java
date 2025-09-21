package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ResetPasswordActivity extends AppCompatActivity {
    private TextView messageTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String email = getIntent().getStringExtra("email");
        String otp = getIntent().getStringExtra("otp");
        if(email == null || otp == null) {
            Log.d("Reset Password", "onCreate: email or otp is null " + email + " " + otp);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        EditText newPasswordEt = findViewById(R.id.newPasswordET);
        EditText confirmPasswordEt = findViewById(R.id.confirmPasswordET);
        messageTv = findViewById(R.id.messageTv);
        Button resetPasswordBtn = findViewById(R.id.resetPasswordBtn);

        resetPasswordBtn.setOnClickListener(v -> {
            String newPassword = newPasswordEt.getText().toString().trim();
            String confirmPassword = confirmPasswordEt.getText().toString().trim();

            if(newPassword.isEmpty() || confirmPassword.isEmpty()) {
                messageTv.setText(R.string.please_fill_all_the_fields);
                return;
            }

            if(!newPassword.equals(confirmPassword)) {
                messageTv.setText(R.string.passwords_do_not_match);
                return;
            }

            resetPassword(email, otp, newPassword);
        });
    }

    private void resetPassword(String email, String otp, String newPassword) {
        RetrofitClient
            .getClient()
            .create(AuthAPI.class)
            .newPassword(new AuthRequest.NewPasswordRequest(email, otp, newPassword))
            .enqueue(new Callback<AuthResponses.VerifyOtpResponse>() {
                @Override
                public void onResponse(Call<AuthResponses.VerifyOtpResponse> call, Response<AuthResponses.VerifyOtpResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(ResetPasswordActivity.this, "Password reset successfully.\nPlease login with new password.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                String errorJson = response.errorBody().string();
                                Log.e("Reset Password", "Error response: " + errorJson);
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Password reset failed.");

                                messageTv.setText(errorMessage);

                                Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ResetPasswordActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthResponses.VerifyOtpResponse> call, Throwable t) {

                }
        });
    }
}