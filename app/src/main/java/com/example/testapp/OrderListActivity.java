package com.example.testapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.OrdersListAdapter;
import com.example.testapp.interfaces.OrderAPI;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.OrderResponses;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderListActivity extends BaseActivity {

    private RecyclerView ordersRecyclerView;
    private TextView emptyOrdersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Orders");

        ordersRecyclerView = findViewById(R.id.orders_list_rv);
        emptyOrdersTextView = findViewById(R.id.emptyOrdersTextView);

        fetchNewOrders();
    }

    private void fetchNewOrders() {
        Calendar calendar = GregorianCalendar.getInstance(); // Or just Calendar.getInstance();
        String finalDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime());

        calendar.add(Calendar.MONTH, -1);
        String initDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime()); // before 1 months

        RetrofitClient
            .getAuthClient(getUserToken())
            .create(OrderAPI.class)
            .getAllOrders(initDate, finalDate)
            .enqueue(new Callback<OrderResponses.AllOrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponses.AllOrderResponse> call, Response<OrderResponses.AllOrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getOrders().isEmpty()) {
                            emptyOrdersTextView.setVisibility(View.VISIBLE);
                            ordersRecyclerView.setVisibility(View.GONE);
                            return;
                        }
                        OrdersListAdapter adapter = new OrdersListAdapter(response.body().getOrders(), OrderListActivity.this);
                        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(OrderListActivity.this));
                        ordersRecyclerView.setAdapter(adapter);
                    } else {
                        try {
                            Log.d("OrderList", "onResponse: " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<OrderResponses.AllOrderResponse> call, Throwable t) {
                    Log.d("Orders List", "onFailure: " + t);
                }
            });
    }
}