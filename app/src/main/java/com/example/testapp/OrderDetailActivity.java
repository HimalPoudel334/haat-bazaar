package com.example.testapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.interfaces.OrderAPI;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.OrderResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends BaseActivity {

    private static final String TAG = "OrderDetailActivity";
    private String orderId;

    private TextView orderIdTv, customerNameTv,  orderDateTv, deliveryLocationTv, deliveryStatusTv,
            orderStatusTv, paymentMethodTv, paymentStatusTv, transactionIdTv, payAmountTv,
            shipmentIdTv, shipmentStatusTv, shipmentDateTv, shipmentAddressTv, orderDeliveryChargeTv, totalAmountTv,
            discountTv, grandTotalTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Order Details");
        orderId = getIntent().getStringExtra("orderId");
        if (orderId == null) finish();



        fetchOrderDetails();
    }

    private void fetchOrderDetails() {
        RetrofitClient
            .getAuthClient(getUserToken())
                .create(OrderAPI.class)
                .getOrder(orderId)
                .enqueue(new Callback<OrderResponses.SingleOrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponses.SingleOrderResponse> call, Response<OrderResponses.SingleOrderResponse> response) {

                    }

                   @Override
                    public void onFailure(Call<OrderResponses.SingleOrderResponse> call, Throwable t) {
                    }
                });
    }
}