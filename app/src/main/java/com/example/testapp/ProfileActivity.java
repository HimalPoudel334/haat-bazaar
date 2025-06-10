package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.UserOrdersAdapter;
import com.example.testapp.interfaces.OrderAPI;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.models.User;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.OrderResponses;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends BaseActivity {

    private TextView fullName, mobileNumber, email, location, emptyOrdersTv;
    private Button changePassword, editProfile, logout;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "My Profile");

        fullName = findViewById(R.id.full_name);
        mobileNumber = findViewById(R.id.mobile_number);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        changePassword = findViewById(R.id.change_password);
        editProfile = findViewById(R.id.edit_profile);
        emptyOrdersTv = findViewById(R.id.empty_orders_tv);

        emptyOrdersTv.setOnClickListener( v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        currentUser = getCurrentUser();
        fullName.setText(String.format("%s %s", currentUser.getFirstName(), currentUser.getLastName()));
        mobileNumber.setText(currentUser.getPhoneNumber());
        email.setText(currentUser.getEmail());
        location.setText(currentUser.getLocation());

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            AuthManager.getInstance().logout();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        showUserOrders();
    }

    private void showUserOrders() {
        RecyclerView ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // call api
        Retrofit retrofit = RetrofitClient.getAuthClient(getUserToken());
        OrderAPI orderAPI = retrofit.create(OrderAPI.class);
        orderAPI.getUserOrders(currentUser.getId()).enqueue(new Callback<OrderResponses.MultiOrderResponses>() {
            @Override
            public void onResponse(Call<OrderResponses.MultiOrderResponses> call, Response<OrderResponses.MultiOrderResponses> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getOrders().isEmpty()) {
                        ordersRecyclerView.setVisibility(View.GONE);
                        emptyOrdersTv.setVisibility(View.VISIBLE);
                        return;
                    }
                    UserOrdersAdapter adapter = new UserOrdersAdapter(ProfileActivity.this, response.body().getOrders());
                    ordersRecyclerView.setAdapter(adapter);
                } else {
                    Log.d("Profile", "onResponse: Failed " + response.message());
                }
            }

            @Override
            public void onFailure(Call<OrderResponses.MultiOrderResponses> call, Throwable t) {

            }
        });

    }
}