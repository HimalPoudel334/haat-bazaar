package com.example.testapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.UserListAdapter;
import com.example.testapp.apis.UserAPI;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.UserResponses;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends BaseActivity {
    private static final String TAG = "UserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activateToolbar(true, "Users");

        fetchAllUsers();
    }

    private void fetchAllUsers() {
        RetrofitClient.getAuthClient(getUserToken()).create(UserAPI.class).getUsers().enqueue(new Callback<UserResponses.MultiUserResponse>() {
            @Override
            public void onResponse(Call<UserResponses.MultiUserResponse> call, Response<UserResponses.MultiUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecyclerView rv = findViewById(R.id.user_list_rv);
                    rv.setLayoutManager(new LinearLayoutManager(UserActivity.this, LinearLayoutManager.VERTICAL, false));
                    rv.setAdapter(new UserListAdapter(UserActivity.this, response.body().getUsers()));
                } else {
                    try {
                        Log.e(TAG, "Failed to fetch all users: " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponses.MultiUserResponse> call, Throwable t) {
                Log.e(TAG, "Failed to fetch all users" + t.getMessage());
            }
        });
    }
}
