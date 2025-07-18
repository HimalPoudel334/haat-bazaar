package com.example.testapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.apis.PaymentAPI;
import com.example.testapp.models.Payment;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.PaymentResponses;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailActivity extends BaseActivity {

    private static final String TAG = "PaymentDetailActivity";
    private Payment payment;

    private Button completeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Payment Details");

        payment = (Payment) getIntent().getParcelableExtra("payment");
        Log.d(TAG, "onCreate: payment is " + RetrofitClient.getGson().toJson(payment));


        ((TextView) findViewById(R.id.payment_id)).setText(String.format(Locale.ENGLISH, "Id: %s", payment.getId()));
        ((TextView) findViewById(R.id.customer_name)).setText(String.format(Locale.ENGLISH, "Customer Name: %s", payment.getUserId()));
        ((TextView) findViewById(R.id.payment_method)).setText(String.format(Locale.ENGLISH, "Payment Method: %s", payment.getPaymentMethod()));
        ((TextView) findViewById(R.id.transaction_id)).setText(String.format(Locale.ENGLISH, "Transaction Id: %s", payment.getTransactionId()));
        ((TextView) findViewById(R.id.payment_status)).setText(String.format(Locale.ENGLISH, "Payment Status: %s", payment.getStatus()));
        ((TextView) findViewById(R.id.amount)).setText(String.format(Locale.ENGLISH, "Amount: Rs. %.2f", payment.getAmount()));
        completeBtn = findViewById(R.id.complete_btn);
        if(payment.getStatus().equals("Pending")) {
            completeBtn.setVisibility(View.VISIBLE);
            completeBtn.setOnClickListener(v -> {
                completePayment();
            });
        }
    }

    private void completePayment() {
        RetrofitClient.getAuthClient(getUserToken())
            .create(PaymentAPI.class)
            .completePayment(payment.getId())
            .enqueue(new Callback<PaymentResponses.MultiPaymentResponse>() {
                @Override
                public void onResponse(Call<PaymentResponses.MultiPaymentResponse> call, Response<PaymentResponses.MultiPaymentResponse> response) {
                    if(response.isSuccessful() && response.code() == 200) {
                        payment.setStatus("Completed");
                        completeBtn.setText(R.string.payment_completed);
                        completeBtn.setEnabled(false);
                        Toast.makeText(PaymentDetailActivity.this, "Payment Completed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<PaymentResponses.MultiPaymentResponse> call, Throwable t) {

                }
        });
    }
}