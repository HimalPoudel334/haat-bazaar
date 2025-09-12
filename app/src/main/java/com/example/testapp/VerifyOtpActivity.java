package com.example.testapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.ForegroundColorSpan;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {

    EditText[] otpFields;
    TextView messageTv;
    Button verifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        verifyBtn = findViewById(R.id.submitOtpBtn);
        verifyBtn.setEnabled(false);

        messageTv = findViewById(R.id.messageTv);

        String email = getIntent().getStringExtra("email");
        String otpMessage = getIntent().getStringExtra("message");
        int expiresInMinutes = getIntent().getIntExtra("expiresInMinutes", 2);

        TextView otpMessageTv = findViewById(R.id.otpMessageTv);
        otpMessageTv.setText(otpMessage);

        TextView otpExpiryTimer = findViewById(R.id.otpExpiryTimer);
        TextView resendTimer = findViewById(R.id.resendTimer);


        // --- OTP Expiry Countdown: 2 minutes (120,000ms) ---
        new CountDownTimer(expiresInMinutes * 60L * 1000, 1000) { // 2 mins
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                otpExpiryTimer.setText(String.format(Locale.ENGLISH, "OTP expires in: %02d:%02d", minutes, remainingSeconds));
            }

            public void onFinish() {
                otpExpiryTimer.setText(R.string.otp_has_expired);
                verifyBtn.setEnabled(false);

            }
        }.start();

        new CountDownTimer(40000, 1000) {
            public void onTick(long millisUntilFinished) {
                resendTimer.setText(String.format(Locale.ENGLISH, "Did not receive the OTP? Request again in %d seconds", millisUntilFinished / 1000));
            }

            public void onFinish() {
                String fullText = "Didn't receive the OTP? Tap to resend";
                SpannableString spannable = new SpannableString(fullText);

                int start = fullText.indexOf("Tap");
                int end = fullText.length();

                spannable.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                int color = ContextCompat.getColor(VerifyOtpActivity.this, com.google.android.material.R.color.design_default_color_on_primary);
                spannable.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                resendTimer.setText(spannable);
                resendTimer.setOnClickListener(v -> {
                    // TODO: Resend OTP logic here
                    Toast.makeText(VerifyOtpActivity.this, "OTP resent", Toast.LENGTH_SHORT).show();
                    // Optionally restart the resend timer
                });
            }
        }.start();

        // Initialize OTP input fields
        otpFields = new EditText[]{
                findViewById(R.id.otp_1),
                findViewById(R.id.otp_2),
                findViewById(R.id.otp_3),
                findViewById(R.id.otp_4),
                findViewById(R.id.otp_5),
                findViewById(R.id.otp_6)
        };

        setupOtpInputs();

        verifyBtn.setOnClickListener(v -> {
            // Optional: collect the OTP
            StringBuilder otpBuilder = new StringBuilder();
            for (EditText editText : otpFields) {
                otpBuilder.append(editText.getText().toString());
            }
            String otp = otpBuilder.toString();
            if(otp.length() != 6) return;

            validateOtpWithBackend(email, otp);
        });
    }

    private void validateOtpWithBackend(String email, String otp) {
        Log.d("VerifyOTP", "validateOtpWithBackend called with - email: " + email + ", otp: " + otp);

        if (email == null || otp == null) {
            Log.e("VerifyOTP", "Email or OTP is null, cannot proceed");
            return;
        }

        RetrofitClient
            .getClient()
            .create(AuthAPI.class)
            .verifyOtp(new AuthRequest.VerifyOtpRequest(email, otp))
            .enqueue(new Callback<AuthResponses.VerifyOtpResponse>() {
                @Override
                public void onResponse(Call<AuthResponses.VerifyOtpResponse> call, Response<AuthResponses.VerifyOtpResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponses.VerifyOtpResponse verifyOtpResponse = response.body();

                        Log.d("Verify Otp", "onResponse: " + verifyOtpResponse.getMessage() + "is valid: " + verifyOtpResponse.isValid());

                        if (verifyOtpResponse.isValid()) {
                            Intent intent = new Intent(VerifyOtpActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("otp", otp);
                            intent.putExtra("email", email);

                            startActivity(intent);
                            finish();
                        } else {
                            messageTv.setText(verifyOtpResponse.getMessage());
                            messageTv.setVisibility(View.VISIBLE);
                        }

                    } else {
                        try {
                            if (response.errorBody() != null) {
                                String errorJson = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "OTP verification failed.");

                                Toast.makeText(VerifyOtpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                Log.e("Verify Otp", "Error response: " + errorMessage);
                            } else {
                                Toast.makeText(VerifyOtpActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VerifyOtpActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onFailure(Call<AuthResponses.VerifyOtpResponse> call, Throwable t) {

                }
        });
    }

    private void setupOtpInputs() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;

            otpFields[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();

                    // Handle paste operation (multiple characters)
                    if (text.length() > 1) {
                        handlePastedText(text);
                        return;
                    }

                    // Handle single character input - move to next field
                    if (text.length() == 1) {
                        // Add a subtle scale animation for single character input
                        otpFields[index].animate()
                                .scaleX(1.05f)
                                .scaleY(1.05f)
                                .setDuration(250)
                                .withEndAction(() -> {
                                    otpFields[index].animate()
                                            .scaleX(1.0f)
                                            .scaleY(1.0f)
                                            .setDuration(250)
                                            .start();
                                })
                                .start();

                        if (index < otpFields.length - 1) {
                            // Small delay to let the animation start before moving focus
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                otpFields[index + 1].requestFocus();
                            }, 100);
                        }
                    }
                    // Handle backspace - move to previous field
                    else if (text.isEmpty() && index > 0) {
                        otpFields[index - 1].requestFocus();
                    }

                    checkAndEnableButton();
                }
            });

            // Set cursor position when field gains focus
            otpFields[index].setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    otpFields[index].setSelection(otpFields[index].getText().length());
                }
            });

            // Add input filter to restrict to single digit during normal typing
            otpFields[index].setFilters(new InputFilter[]{
                    (source, start, end, dest, dstart, dend) -> {
                        // Allow paste operations (longer text will be handled by TextWatcher)
                        if (source.length() > 1) {
                            return null; // Let it pass to TextWatcher
                        }

                        // For single characters, ensure only digits and max 1 character
                        if (source.length() == 1) {
                            if (Character.isDigit(source.charAt(0))) {
                                // If field already has text and we're adding (not replacing), block it
                                if (dest.length() >= 1 && dend - dstart == 0) {
                                    return ""; // Block additional characters
                                }
                                return null; // Allow the digit
                            } else {
                                return ""; // Block non-digits
                            }
                        }

                        return null; // Allow other operations (like deletion)
                    }
            });
        }
    }

    private void handlePastedText(String pastedText) {
        // Extract only digits from pasted text
        String digitsOnly = pastedText.replaceAll("[^0-9]", "");

        // Clear all fields first
        for (EditText field : otpFields) {
            field.setText("");
        }

        // Fill fields with digits
        for (int i = 0; i < otpFields.length && i < digitsOnly.length(); i++) {
            otpFields[i].setText(String.valueOf(digitsOnly.charAt(i)));
        }

        // Set focus to the next empty field or the last field if all are filled
        int nextFocusIndex = Math.min(digitsOnly.length(), otpFields.length - 1);
        if (nextFocusIndex < otpFields.length) {
            otpFields[nextFocusIndex].requestFocus();
        }

        checkAndEnableButton();
    }

    private boolean areAllOtpFieldsFilled() {
        if (otpFields == null) { // Should not happen after setupOtpInputs, but good for safety
            return false;
        }
        for (EditText editText : otpFields) {
            if (editText.getText().toString().trim().isEmpty()) {
                return false; // If any field is empty, return false
            }
        }
        return true; // All fields are filled
    }

    // New method to centralize button state check
    private void checkAndEnableButton() {
        if (verifyBtn != null) { // Ensure button is initialized
            verifyBtn.setEnabled(areAllOtpFieldsFilled());
        }
    }
}