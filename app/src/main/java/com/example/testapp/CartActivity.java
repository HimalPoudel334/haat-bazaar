package com.example.testapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.CartRecyclerViewAdapter;
import com.example.testapp.basetypes.Location;
import com.example.testapp.interfaces.CartAPI;
import com.example.testapp.interfaces.CustomerAPI;
import com.example.testapp.interfaces.OrderAPI;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Customer;
import com.example.testapp.models.Order;
import com.example.testapp.models.OrderDetail;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.CartResponses;
import com.example.testapp.responses.CustomerResponses;
import com.example.testapp.responses.OrderResponses;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends BaseActivity implements CartRecyclerViewAdapter.OnCheckBoxStateChangeListener {

    private final List<Cart> carts = new ArrayList<>();
    private CartRecyclerViewAdapter adapter;
    Button buttonRemove, buttonCheckout;
    TextView productPriceTv, totalChargeTv, deliveryChargeTv, emptyCartTextView;

    private final double DELIVERY_CHARGE = 100.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        //setup toolbar
        activateToolbar(true);

        buttonCheckout = findViewById(R.id.button_checkout);
        buttonRemove = findViewById(R.id.button_remove);
        productPriceTv = findViewById(R.id.product_price_tv);
        totalChargeTv = findViewById(R.id.total_charge_tv);
        deliveryChargeTv = findViewById(R.id.delivery_charge_tv);
        emptyCartTextView = findViewById(R.id.emptyCartTextView);

        // Initialize the RecyclerView and its adapter
        RecyclerView cartRecyclerView = findViewById(R.id.cart_rv);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CartRecyclerViewAdapter(carts, this, this);
        cartRecyclerView.setAdapter(adapter);

        // Make the API call
        Retrofit retrofit = RetrofitClient.getClient();
        CartAPI cartAPI = retrofit.create(CartAPI.class);
        cartAPI.getCustomerCart(RetrofitClient.CURRENT_CUSTOMER_ID).enqueue(new Callback<CartResponses.MultiCartResponse>() {
            @Override
            public void onResponse(Call<CartResponses.MultiCartResponse> call, Response<CartResponses.MultiCartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().getCarts().isEmpty()) {
                        cartRecyclerView.setVisibility(View.GONE);
                        emptyCartTextView.setVisibility(View.VISIBLE);
                        return;
                    }

                    Log.d("Cart", "onResponse: success cart " + response.body().getCarts().get(0).getRate());
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
                Log.d("Cart Activity", "onCheckBoxStateChanged: cart price: "+c.getPrice());
                totalCharge[0] += c.getPrice();
            }
            Retrofit retrofit = RetrofitClient.getClient();
            CartAPI cartAPI = retrofit.create(CartAPI.class);
            buttonRemove.setOnClickListener(view -> {

                for(Cart c : selectedCarts) {
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
            });

            productPriceTv.setText(String.format(": Rs %s", totalCharge[0]));
            deliveryChargeTv.setText(String.format(": Rs %s", DELIVERY_CHARGE));
            totalChargeTv.setText(String.format(": Rs %s", totalCharge[0] + DELIVERY_CHARGE));

            //TODO: get customer id from db or current logged in user
            //lets hardcode the customerId here for now
            Customer customer = new Customer();
            customer.setId(RetrofitClient.CURRENT_CUSTOMER_ID);
            customer.setLocation(new Location("Jhapa", "Birtamod", "Birtamod", 9, "Khamtelbaari"));

            buttonCheckout.setOnClickListener(v -> {
                List<OrderDetail> orderDetails = new ArrayList<>();
                Order order = new Order(customer, customer.getLocation(), 100);
                for(Cart c : selectedCarts) {
                    OrderDetail detail = new OrderDetail(order, c.getProductId(), c.getQuantity());
                    detail.setPrice(c.getQuantity() * c.getRate());
                    orderDetails.add(detail);
                }
                order.setOrderDetails(orderDetails);

                OrderAPI orderAPI = retrofit.create(OrderAPI.class);
                Gson gson = RetrofitClient.getGson();
                Log.d("Cart order create", "Order object is"+gson.toJson(order));
                orderAPI.createOrder(order).enqueue(new Callback<OrderResponses.SingleOrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponses.SingleOrderResponse> call, Response<OrderResponses.SingleOrderResponse> response) {
                        if(response.isSuccessful() && response.body().getOrder() != null) {
                            Toast.makeText(getApplicationContext(), "Order created successfully", Toast.LENGTH_SHORT).show();
                            cartAPI.deleteCustomerCart(RetrofitClient.CURRENT_CUSTOMER_ID).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    for(Cart c : selectedCarts) {
                                        carts.remove(c);
                                        adapter.notifyItemRemoved(carts.indexOf(c));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.d("Cart delete", "onFailure: "+t.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponses.SingleOrderResponse> call, Throwable t) {
                        Log.d("Create order from cart", "onFailure: "+t.getMessage());
                    }
                });

            });
        } else {
            buttonCheckout.setEnabled(false);
            buttonRemove.setEnabled(false);
            productPriceTv.setText("");
            totalChargeTv.setText("");
            deliveryChargeTv.setText("");
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
