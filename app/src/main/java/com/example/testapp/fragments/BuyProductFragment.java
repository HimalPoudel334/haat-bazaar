package com.example.testapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.testapp.BaseActivity;
import com.example.testapp.CartActivity;
import com.example.testapp.R;
import com.example.testapp.interfaces.OrderAPI;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.models.Cart;
import com.example.testapp.models.User;
import com.example.testapp.models.Order;
import com.example.testapp.models.OrderItem;
import com.example.testapp.models.Product;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.paymentgateway.EsewaPaymentGateway;
import com.example.testapp.responses.OrderResponses;
import com.f1soft.esewapaymentsdk.EsewaConfiguration;
import com.f1soft.esewapaymentsdk.EsewaPayment;
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private final ActivityResultLauncher<Intent> registerActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("TAGz", "result " + result.getResultCode());
                switch (result.getResultCode()) {
                    case Activity.RESULT_OK:
                        String resultMessage = result.getData().getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE);
                        if (resultMessage != null) {
                            Log.i("Proof of Payment", resultMessage);
                        }
                        Toast.makeText(getContext(), "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getContext(), "Canceled By User", Toast.LENGTH_SHORT).show();
                        break;
                    case EsewaPayment.RESULT_EXTRAS_INVALID:
                        resultMessage = result.getData().getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE);
                        if (resultMessage != null) {
                            Toast.makeText(getContext(), resultMessage, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
    );

    public BuyProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.close_fragment_icon).setOnClickListener(v -> {
            this.dismiss();
        });

        ImageView productImage = view.findViewById(R.id.product_iv);
        Glide.with(getContext())
                .load(product.getImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(productImage);

        TextView productPrice = view.findViewById(R.id.product_price_tv);
        productPrice.setText(String.format("%s per %s", product.getPrice(), product.getUnit()));

        TextView quantityTextView = view.findViewById(R.id.quantity_tv);

        ImageView quantityPlus = view.findViewById(R.id.quantity_plus);
        quantityPlus.setOnClickListener(v -> updateQuantity(quantityTextView, product.getUnitChange()));

        ImageView quantityMinus = view.findViewById(R.id.quantity_minus);
        quantityMinus.setOnClickListener(v -> updateQuantity(quantityTextView, -product.getUnitChange()));

        TextView productPreviousPrice = view.findViewById(R.id.product_prev_price);
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
            if(deliveryLocation.isEmpty()) {
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

        User user = AuthManager.getInstance().getCurrentUser();

        Order order = new Order(user, deliveryLocation, deliveryCharge);
        List<OrderItem> OrderItems = new ArrayList<>();

        OrderItem detail = new OrderItem(order, product, quantity);
        OrderItems.add(detail);

        order.setOrderItems(OrderItems);
        Log.d("Create Order", "inside createOrder: order with order detail created:");
        Gson gson = RetrofitClient.getGson();
        Log.d("Place order", "createOrder: "+gson.toJson(order));

        //make payment to esewa
        makeEsewaPayment(""+order.getTotalPrice(), product.getName(), product.getId(), "");

        /*OrderAPI orderAPI = RetrofitClient.getClient().create(OrderAPI.class);
        orderAPI.createOrder(order).enqueue(new Callback<OrderResponses.SingleOrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponses.SingleOrderResponse> call, Response<OrderResponses.SingleOrderResponse> response) {
                if(response.body() != null)
                    Log.d("Create Order", "onResponse: " + response.body().getOrder().getOrderFulfilledDate());
                try {
                    Log.d("Create Order", "onResponse: "+response.errorBody().string());
                } catch (IOException | NullPointerException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<OrderResponses.SingleOrderResponse> call, Throwable t) {
                Log.d("Create Order", "onFailure: " + t.getMessage());
            }
        });
*/
        Log.d("Create Order", "inside createOrder: api call done:");


    }

    private void makeEsewaPayment(String amount, String productName, String productId, String callbackUrl) {
        EsewaPayment eSewaPayment = new EsewaPayment(amount, productName, productId + System.nanoTime(), callbackUrl, null);
        Log.d("Esewa fragment", "makeEsewaPayment: called");
        Intent intent = new Intent(getActivity(), EsewaPaymentActivity.class);
        intent.putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, EsewaPaymentGateway.getEsewaConfiguration());
        intent.putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment);
        registerActivity.launch(intent);
    }
}