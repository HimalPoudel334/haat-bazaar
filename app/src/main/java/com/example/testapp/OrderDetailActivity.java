package com.example.testapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.OrderItemListAdapter;
import com.example.testapp.helpers.BaseTypeHelper;
import com.example.testapp.apis.OrderAPI;
import com.example.testapp.models.Order;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.OrderResponses;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends BaseActivity {

    private static final String TAG = "OrderDetailActivity";
    private String orderId;
    private Order order;

    private TextView orderIdTv, customerNameTv,  orderDateTv, deliveryLocationTv, deliveryStatusTv,
            orderStatusTv, paymentMethodTv, paymentStatusTv, transactionIdTv, payAmountTv,
            shipmentIdTv, shipmentStatusTv, shipmentDateTv, shipmentAddressTv, orderDeliveryChargeTv, totalAmountTv,
            discountTv, grandTotalTv;

    private Button deliveryStatusSaveBtn, orderStatusSaveBtn, shipmentStatusSaveBtn, paymentStatusSaveBtn;
    private Spinner deliveryStatusSpinner, orderStatusSpinner, shipmentStatusSpinner, paymentStatusSpinner;
    private ArrayAdapter<String> paymentStatusAdapter;



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
        Log.d(TAG, "onCreate: orderId: " + orderId);
        if (orderId == null) finish();

        orderIdTv = findViewById(R.id.order_id);
        customerNameTv = findViewById(R.id.customer_name);
        orderDateTv = findViewById(R.id.order_date);
        deliveryLocationTv = findViewById(R.id.delivery_location);
        deliveryStatusTv = findViewById(R.id.delivery_status);
        orderStatusTv = findViewById(R.id.order_status);
        paymentMethodTv = findViewById(R.id.payment_method);
        paymentStatusTv = findViewById(R.id.payment_status);
        transactionIdTv = findViewById(R.id.transaction_id);
        payAmountTv = findViewById(R.id.pay_amount);
        shipmentIdTv = findViewById(R.id.shipment_id);
        shipmentStatusTv = findViewById(R.id.shipment_status);
        shipmentDateTv = findViewById(R.id.shipment_date);
        shipmentAddressTv = findViewById(R.id.shipment_address);
        orderDeliveryChargeTv = findViewById(R.id.delivery_charge);
        totalAmountTv = findViewById(R.id.total_amount);
        discountTv = findViewById(R.id.discount);
        grandTotalTv = findViewById(R.id.grand_total);

        deliveryStatusSaveBtn = findViewById(R.id.delivery_status_button);
        deliveryStatusSaveBtn.setOnClickListener(v -> {
            String status = deliveryStatusSpinner.getSelectedItem().toString();
            updateDeliveryStatus(status);
        });

        orderStatusSaveBtn = findViewById(R.id.order_status_button);
        orderStatusSaveBtn.setOnClickListener(v -> {
            String status = orderStatusSpinner.getSelectedItem().toString();
            updateOrderStatus(status);
        });

        shipmentStatusSaveBtn = findViewById(R.id.shipment_status_button);
        shipmentStatusSaveBtn.setOnClickListener(v -> {
            String status = shipmentStatusSpinner.getSelectedItem().toString();
            updateShipmentStatus(status);
        });

        paymentStatusSaveBtn = findViewById(R.id.payment_status_button);
        paymentStatusSaveBtn.setOnClickListener(v -> {
            String status = paymentStatusSpinner.getSelectedItem().toString();
            updatePaymentStatus(status);
        });

        deliveryStatusSpinner = findViewById(R.id.delivery_status_spinner);
        orderStatusSpinner = findViewById(R.id.order_status_spinner);
        shipmentStatusSpinner = findViewById(R.id.shipment_status_spinner);
        paymentStatusSpinner = findViewById(R.id.payment_status_spinner);

        String[] paymentStatusList = {"Completed", "Pending"};
        paymentStatusAdapter = new ArrayAdapter<>(
                OrderDetailActivity.this, android.R.layout.simple_spinner_item, paymentStatusList
        );
        paymentStatusAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        paymentStatusSpinner.setAdapter(paymentStatusAdapter);
        paymentStatusSpinner.setSelection(paymentStatusAdapter.getPosition("Pending"));

        fetchOrderDetails();
    }

    private void updatePaymentStatus(String status) {
        Toast.makeText(OrderDetailActivity.this, "Update Payment Status with: " + status, Toast.LENGTH_SHORT).show();
    }

    private void updateShipmentStatus(String status) {
        Toast.makeText(OrderDetailActivity.this, "Update Shipment Status with: " + status, Toast.LENGTH_SHORT).show();
    }

    private void updateOrderStatus(String status) {
        Toast.makeText(OrderDetailActivity.this, "Update Order Status with: " + status, Toast.LENGTH_SHORT).show();
    }

    private void updateDeliveryStatus(String status) {
        Toast.makeText(OrderDetailActivity.this, "Update Delivery Status with: " + status, Toast.LENGTH_SHORT).show();
    }

    private void fetchOrderDetails() {
        RetrofitClient
            .getAuthClient(getUserToken())
                .create(OrderAPI.class)
                .getOrder(orderId)
                .enqueue(new Callback<OrderResponses.SingleOrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponses.SingleOrderResponse> call, Response<OrderResponses.SingleOrderResponse> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            order = response.body().getOrder();
                            setupOrderDetails();

                            fetchDeliveryStatuses(order.getDeliveryStatus());
                            fetchOrderStatues(order.getStatus());
                            fetchShipmentStatuses(
                                    order.getShipment() != null ? order.getShipment().getStatus() : null
                            );
                            if (order.getPayment() != null && Objects.equals(order.getPayment().getStatus(), "Completed")) {
                                paymentStatusSpinner.setSelection(paymentStatusAdapter.getPosition(order.getPayment().getStatus()));
                                paymentStatusSpinner.setEnabled(false);
                                paymentStatusSaveBtn.setEnabled(false);
                            }


                        } else {
                            try {
                                Log.d(TAG, "onResponse: " + response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                   @Override
                    public void onFailure(Call<OrderResponses.SingleOrderResponse> call, Throwable t) {
                       Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    private void setupOrderDetails() {
        Log.d(TAG, "setupOrderDetails: order is " + RetrofitClient.getGson().toJson(order));
        orderIdTv.setText(String.format(Locale.ENGLISH, "Order Id: %s", order.getId()));
        customerNameTv.setText(String.format(Locale.ENGLISH, "Customer Name: %s", order.getUser().getFirstName() + " " + order.getUser().getLastName()));
        orderDateTv.setText(String.format(Locale.ENGLISH, "Order Date: %s", order.getOrderCreationDate()));
        deliveryLocationTv.setText(String.format(Locale.ENGLISH, "Delivery Location: %s", order.getDeliveryLocation()));
        deliveryStatusTv.setText(R.string.delivery_status);
        orderStatusTv.setText(R.string.order_status);
        if (order.getPayment() != null) {
            paymentMethodTv.setText(String.format(Locale.ENGLISH, "Payment Method: %s", order.getPayment().getPaymentMethod()));
            transactionIdTv.setText(String.format(Locale.ENGLISH, "Transaction Id: %s", order.getPayment().getTransactionId()));
            paymentStatusTv.setText(R.string.payment_status);
            payAmountTv.setText(String.format(Locale.ENGLISH, "Pay Amount: %s", order.getPayment().getAmount()));
        } else {
            transactionIdTv.setText(R.string.transaction_id_n_a);
            paymentStatusTv.setText(R.string.payment_status);
            payAmountTv.setText(R.string.pay_amount_n_a);
        }

        if (order.getShipment() != null) {
            shipmentIdTv.setText(String.format(Locale.ENGLISH, "Shipment Id: %s", order.getShipment().getId()));
            shipmentStatusTv.setText(R.string.shipment_status);
            shipmentDateTv.setText(String.format(Locale.ENGLISH, "Shipment Date: %s", order.getShipment().getShipDate()));
            shipmentAddressTv.setText(String.format(Locale.ENGLISH, "Shipment Address: %s", order.getShipment().getAddress()));

        } else {
            shipmentIdTv.setText(R.string.shipment_id_n_a);
            shipmentStatusTv.setText(R.string.shipment_status);
            shipmentDateTv.setText(R.string.shipment_date_n_a);
            shipmentAddressTv.setText(R.string.shipmentaddress_n_a);
        }

        totalAmountTv.setText(String.format(Locale.ENGLISH, "Total Amount: Rs. %s", order.getTotalPrice() - order.getDeliveryCharge()));
        orderDeliveryChargeTv.setText(String.format(Locale.ENGLISH, "Delivery Charge: Rs. %s", order.getDeliveryCharge()));
        discountTv.setText(String.format(Locale.ENGLISH, "Discount: Rs. %s", order.getDiscount()));
        grandTotalTv.setText(String.format(Locale.ENGLISH, "Grand Total: %s", order.getTotalAmount()));

        OrderItemListAdapter adapter = new OrderItemListAdapter(order.getOrderItems(), OrderDetailActivity.this);
        RecyclerView recyclerView = findViewById(R.id.order_item_list_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private void fetchShipmentStatuses(String status) {
        BaseTypeHelper.getShipmentStatus(getUserToken(), shipmentStatusList -> {
            if (shipmentStatusList.isEmpty()) return;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    OrderDetailActivity.this, android.R.layout.simple_spinner_item, shipmentStatusList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            shipmentStatusSpinner.setAdapter(adapter);
            shipmentStatusSpinner.setSelection(adapter.getPosition(status));

        });
    }

    private void fetchOrderStatues(String status) {
        BaseTypeHelper.getOrderStatus(getUserToken(), orderStatusList -> {
            if (orderStatusList.isEmpty()) return;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    OrderDetailActivity.this, android.R.layout.simple_spinner_item, orderStatusList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            orderStatusSpinner.setAdapter(adapter);
            orderStatusSpinner.setSelection(adapter.getPosition(status));
        });
    }

    private void fetchDeliveryStatuses(String status) {
        BaseTypeHelper.getDeliveryStatus(getUserToken(), deliveryStatusList -> {
            if (deliveryStatusList.isEmpty()) return;

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    OrderDetailActivity.this, android.R.layout.simple_spinner_item, deliveryStatusList
            );
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            deliveryStatusSpinner.setAdapter(adapter);
            deliveryStatusSpinner.setSelection(adapter.getPosition(status));
        });
    }
}