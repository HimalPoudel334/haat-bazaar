package com.example.testapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.EntityAdapter;
import com.example.testapp.interfaces.OrderAPI;
import com.example.testapp.models.Entity;
import com.example.testapp.models.Order;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.OrderResponses;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanelActivity extends BaseActivity {

    private RecyclerView entityRecyclerView;
    private List<Entity> entityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        activateToolbar(true, "Admin Panel");

        entityRecyclerView = findViewById(R.id.entity_recycler_view);

        entityList = new ArrayList<>();
        entityList.add(new Entity("Orders", R.drawable.new_order));
        entityList.add(new Entity("Categories", R.drawable.category));
        entityList.add(new Entity("Products", R.drawable.products));
        entityList.add(new Entity("Payment", R.drawable.payment));
        entityList.add(new Entity("Shipment", R.drawable.shipment));
        entityList.add(new Entity("Inventory", R.drawable.inventory));
        entityList.add(new Entity("Reports", R.drawable.report));
        entityList.add(new Entity("Users", R.drawable.users));
        entityList.add(new Entity("Settings", R.drawable.gear));
        entityList.add(new Entity("Logout", R.drawable.logout));

        fetchNewOrders();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.menu_item_search_view);
        return true;
    }

    private void fetchNewOrders() {

        Calendar calendar = GregorianCalendar.getInstance(); // Or just Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        String initDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.getTime()); // before 10 days
        RetrofitClient.getAuthClient(getUserToken()).create(OrderAPI.class).getNewOrdersCount(initDate, initDate).enqueue(new Callback<OrderResponses.NewOrderCountResponse>() {
            @Override
            public void onResponse(Call<OrderResponses.NewOrderCountResponse> call, Response<OrderResponses.NewOrderCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int newOrdersCount = response.body().getCount();
                    entityRecyclerView.setLayoutManager(new GridLayoutManager(AdminPanelActivity.this, 2));
                    entityRecyclerView.setAdapter(new EntityAdapter(AdminPanelActivity.this, entityList, newOrdersCount));
                }
            }

            @Override
            public void onFailure(Call<OrderResponses.NewOrderCountResponse> call, Throwable t) {
                Log.e("AdminPanelActivity", "Failed to fetch new orders count", t);
            }
        });
    }
}