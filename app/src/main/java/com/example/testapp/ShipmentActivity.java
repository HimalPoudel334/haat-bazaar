package com.example.testapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.ShipmentListAdapter;
import com.example.testapp.interfaces.ShipmentAPI;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.ShipmentResponses;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shipment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Shipments");

        setupMainContent();
    }

    private void setupMainContent() {
        RetrofitClient.getAuthClient(getUserToken()).create(ShipmentAPI.class).getShipments().enqueue(new Callback<ShipmentResponses.MultiShipmentResponse>() {
            @Override
            public void onResponse(Call<ShipmentResponses.MultiShipmentResponse> call, Response<ShipmentResponses.MultiShipmentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecyclerView rv = findViewById(R.id.shipment_list_rv);
                    rv.setLayoutManager(new LinearLayoutManager(ShipmentActivity.this));
                    rv.setAdapter(new ShipmentListAdapter(ShipmentActivity.this, response.body().getShipments()));
                } else {
                    try {
                        Log.d("Shipment Activity", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ShipmentResponses.MultiShipmentResponse> call, Throwable t) {
                Log.d("Shipment Activity", "onFailure: " + t.getMessage());

            }
        });
    }
}