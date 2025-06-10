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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        calendar.add(Calendar.YEAR, -1);

        String initDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime()); // before 10 days
        RetrofitClient.getAuthClient(getUserToken()).create(OrderAPI.class).getAllOrders(initDate, initDate).enqueue(new Callback<OrderResponses.MultiOrderResponses>() {
            @Override
            public void onResponse(Call<OrderResponses.MultiOrderResponses> call, Response<OrderResponses.MultiOrderResponses> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getOrders().isEmpty()) {
                        emptyOrdersTextView.setVisibility(View.VISIBLE);
                        return;
                    }
                    OrdersListAdapter adapter = new OrdersListAdapter(response.body().getOrders(), OrderListActivity.this);
                    ordersRecyclerView.setLayoutManager(new LinearLayoutManager(OrderListActivity.this));
                    ordersRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<OrderResponses.MultiOrderResponses> call, Throwable t) {
                Log.d("Orders List", "onFailure: " + t);
            }
        });
    }
}