package com.example.testapp.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.interfaces.OrderAPI;
import com.example.testapp.models.Customer;
import com.example.testapp.models.Order;
import com.example.testapp.models.OrderDetail;
import com.example.testapp.models.Product;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.OrderResponses;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.internal.TextWatcherAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyProductFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Product product;

    public BuyProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.close_fragment_icon).setOnClickListener(v -> {
            this.dismiss();
        });

        ImageView productImage = view.findViewById(R.id.product_about_to_buy_iv);
        Glide.with(getContext())
                .load(product.getImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(productImage);

        TextView productPrice = view.findViewById(R.id.product_about_to_buy_price_tv);
        productPrice.setText(String.format("%s per %s", product.getPrice(), product.getUnit()));

        TextView quantityTextView = view.findViewById(R.id.quantity_tv);

        ImageView quantityPlus = view.findViewById(R.id.quantity_plus);
        quantityPlus.setOnClickListener(v -> updateQuantity(quantityTextView, product.getUnitChange()));

        ImageView quantityMinus = view.findViewById(R.id.quantity_minus);
        quantityMinus.setOnClickListener(v -> updateQuantity(quantityTextView, -product.getUnitChange()));

        TextView productPreviousPrice = view.findViewById(R.id.product_about_to_buy_prev_price);
        productPreviousPrice.setText(String.format("%s %s", product.getPreviousPrice(), product.getUnit()));
        productPreviousPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        TextView deliveryChargeTextView = view.findViewById(R.id.delivery_charge);
        double deliveryCharge = Double.parseDouble(deliveryChargeTextView.getText().toString().split(" ")[1]);

        TextView totalPriceTextView = view.findViewById(R.id.total_amount);
        totalPriceTextView.setText(""+(product.getPrice() + deliveryCharge));

        quantityTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setTotalPrice(quantityTextView, totalPriceTextView, deliveryCharge);
            }
        });

        Button cancelButton = view.findViewById(R.id.order_cancel_button);
        cancelButton.setOnClickListener(v -> this.dismiss());

        EditText deliveryLocationEditText = view.findViewById(R.id.delivery_location_et);

        Button placeOrderButton = view.findViewById(R.id.place_order_button);
        placeOrderButton.setOnClickListener(v -> {
            double quantity = Double.parseDouble(quantityTextView.getText().toString().split(" ")[0]);
            Log.d("Place Order button", "onViewCreated: " + quantity);
            String deliveryLocation = deliveryLocationEditText.getText().toString();
            if(deliveryLocation.isEmpty() || deliveryLocation.length() == 0) {
                Toast.makeText(getContext(), "Delivery location is required", Toast.LENGTH_SHORT).show();
            } else {
                createOrder(quantity, deliveryLocation, deliveryCharge);
            }

        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param product The product about to be bought.
     * @return A new instance of fragment BuyProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyProductFragment newInstance(Product product) {
        BuyProductFragment fragment = new BuyProductFragment();
        Bundle args = new Bundle();
        args.putParcelable("productAboutToBuy", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getParcelable("productAboutToBuy");
            Log.d("Fragment", "onCreate: product is " + product.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_product, container, false);
    }

    private void updateQuantity(TextView quantityTV, double change) {
        //this logic should be moved to Product model itself.
        double quantity = Double.parseDouble(quantityTV.getText().toString().split(" ")[0]);
        quantity += change;
        if(quantity > 0 && quantity <= product.getStock())
            quantityTV.setText(String.format("%s %s", quantity, product.getUnit()));
        else if (quantity == 0) {
            Toast.makeText(getContext(), "Cannot order less than that", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Out of stock", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTotalPrice(TextView quantityTV, TextView priceTV, double deliveryCharge) {
        double quantity = Double.parseDouble(quantityTV.getText().toString().split(" ")[0]);
        double price = product.getPrice() * quantity;
        double priceWithDeliveryCharge = price + deliveryCharge;
        priceTV.setText("" + priceWithDeliveryCharge);
    }

    private void createOrder(double quantity, String deliveryLocation, double deliveryCharge) {
        Log.d("Create Order", "inside createOrder:");

        //TODO: get customer id from db or current logged in user
        //lets hardcode the customerId here for now
        final String customerId = "56d543ef-e4d4-462c-a37a-3f45c1335cb5";
        Customer customer = new Customer();
        customer.setId(customerId);

        Order order = new Order(customer, deliveryLocation, deliveryCharge);
        List<OrderDetail> orderDetails = new ArrayList<>();

        OrderDetail detail = new OrderDetail(order, product, quantity);
        orderDetails.add(detail);

        order.setOrderDetails(orderDetails);
        Log.d("Create Order", "inside createOrder: order with order detail created:");

        OrderAPI orderAPI = RetrofitClient.getClient().create(OrderAPI.class);
        orderAPI.createOrder(order).enqueue(new Callback<OrderResponses.SingleOrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponses.SingleOrderResponse> call, Response<OrderResponses.SingleOrderResponse> response) {
                if(response.body() != null)
                    Log.d("Create Order", "onResponse: " + response.body().getOrder().getOrderFulfilledDate());
                try {
                    Log.d("Create Order", "onResponse: "+response.errorBody().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<OrderResponses.SingleOrderResponse> call, Throwable t) {
                Log.d("Create Order", "onFailure: " + t.getMessage());
            }
        });

        Log.d("Create Order", "inside createOrder: api call done:");


    }
}