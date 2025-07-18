package com.example.testapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.PaymentListAdapter;
import com.example.testapp.apis.PaymentAPI;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.PaymentResponses;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends BaseActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Payments");

        recyclerView = findViewById(R.id.payment_list_rv);

        setupMainContent();
    }

    private void setupMainContent() {
        Calendar calendar = GregorianCalendar.getInstance(); // Or just Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String finalDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime());
        calendar.add(Calendar.MONTH, -1);
        String initDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime()); // before 1 months

        RetrofitClient.getAuthClient(getUserToken())
            .create(PaymentAPI.class)
            .getAllPayments(initDate, finalDate)
            .enqueue(new Callback<PaymentResponses.MultiPaymentResponse>() {
                @Override
                public void onResponse(Call<PaymentResponses.MultiPaymentResponse> call, Response<PaymentResponses.MultiPaymentResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        PaymentListAdapter adapter = new PaymentListAdapter(PaymentActivity.this, response.body().getPayments());
                        recyclerView.setLayoutManager(new LinearLayoutManager(PaymentActivity.this));
                        recyclerView.setAdapter(adapter);
                        Log.d("Payment List", "onResponse: " + RetrofitClient.getGson().toJson(response.body().getPayments()));
                    } else {
                        try {
                            Log.d("Create Payment Khalti", "onResponse: "+response.errorBody().string());
                        } catch (IOException | NullPointerException e) {
                            Log.d("Payment List", "onResponse: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PaymentResponses.MultiPaymentResponse> call, Throwable t) {
                    Log.d("Payment List", "onFailure: " + t.getMessage());
                }
        });
    }

}