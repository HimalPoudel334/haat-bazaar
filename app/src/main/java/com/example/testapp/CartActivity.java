package com.example.testapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.CartRecyclerViewAdapter;
import com.example.testapp.interfaces.CartAPI;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Product;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.CartResponses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends BaseActivity implements CartRecyclerViewAdapter.OnCheckBoxStateChangeListener {

    private final List<Cart> carts = new ArrayList<>();
    private CartRecyclerViewAdapter adapter;
    Button buttonRemove, buttonCheckout;
    TextView totalChargeTv, deliveryChargeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        //setup toolbar
        activateToolbar(true);

        buttonCheckout = findViewById(R.id.button_checkout);
        buttonRemove = findViewById(R.id.button_remove);
        totalChargeTv = findViewById(R.id.total_charge_tv);
        deliveryChargeTv = findViewById(R.id.delivery_charge_tv);


        final String customerId = "56d543ef-e4d4-462c-a37a-3f45c1335cb5";

        // Initialize the RecyclerView and its adapter
        RecyclerView cartRecyclerView = findViewById(R.id.cart_rv);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CartRecyclerViewAdapter(carts, this, this);
        cartRecyclerView.setAdapter(adapter);

        // Make the API call
        Retrofit retrofit = RetrofitClient.getClient();
        CartAPI cartAPI = retrofit.create(CartAPI.class);
        cartAPI.getCustomerCart(customerId).enqueue(new Callback<CartResponses.MultiCartResponse>() {
            @Override
            public void onResponse(Call<CartResponses.MultiCartResponse> call, Response<CartResponses.MultiCartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Cart", "onResponse: success cart " + response.body().getCarts().get(1).getProductName());
                    int startPosition = carts.size();
                    carts.addAll(response.body().getCarts());
                    // Notify the adapter about the new items
                    adapter.notifyItemRangeInserted(startPosition, response.body().getCarts().size());
                    adapter.setBackupCartList(response.body().getCarts());
                    Log.d("Cart", "onCreate: " + carts.size());
                }
            }

            @Override
            public void onFailure(Call<CartResponses.MultiCartResponse> call, Throwable t) {
                Log.d("Cart", "onFailure: getting cart failed");
            }
        });
    }

    @Override
    public void onCheckBoxStateChanged(List<Cart> selectedCarts) {
        Log.d("Cart Activity", "onCheckBoxStateChanged: "+selectedCarts.size());
        if(!selectedCarts.isEmpty()) {
            buttonCheckout.setEnabled(true);
            buttonRemove.setEnabled(true);
            final double[] totalCharge = {0};
            for(Cart c : selectedCarts) {
                totalCharge[0] += c.getPrice();
            }
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for(Cart c : selectedCarts) {
                        Retrofit retrofit = RetrofitClient.getClient();
                        CartAPI cartAPI = retrofit.create(CartAPI.class);
                        cartAPI.deleteCart(c.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    carts.remove(c);
                                    adapter.notifyItemRemoved(carts.indexOf(c));
                                    // Update total charge display here
                                    totalChargeTv.setText(String.format("Total: Rs %s", totalCharge[0] - c.getPrice()));
                                }

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                }
            });

            totalChargeTv.setText(String.format("Total: Rs %s", totalCharge[0]));
            deliveryChargeTv.setText(String.format("Delivery charge: Rs %s", 100));
        } else {
            buttonCheckout.setEnabled(false);
            buttonRemove.setEnabled(false);
            totalChargeTv.setText(String.format("Total: Rs %s", 0.0));
            deliveryChargeTv.setText(String.format("Delivery charge: Rs %s", 100));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle the item clicked
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_search_view) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Search");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

}
