package com.example.testapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.example.testapp.basetypes.PaymentMethod;
import com.example.testapp.interfaces.KhaltiAPI;
import com.example.testapp.models.Customer;
import com.example.testapp.models.Order;
import com.example.testapp.models.OrderDetail;
import com.example.testapp.models.Product;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.paymentgateway.EsewaPaymentGateway;
import com.example.testapp.paymentgateway.KhaltiPaymentGateway;
import com.example.testapp.responses.KhaltiResponses;
import com.f1soft.esewapaymentsdk.EsewaConfiguration;
import com.f1soft.esewapaymentsdk.EsewaPayment;
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyProductActivity extends BaseActivity {

    private Product product;
    private ActivityResultLauncher<Intent> registerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        activateToolbar(true);

        product = (Product) getIntent().getExtras().getParcelable("productAboutToBuy");
        Log.d("Fragment", "onCreate: product is " + product.getName());

        registerActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("TAGz", "result " + result.getResultCode());
                switch (result.getResultCode()) {
                    case Activity.RESULT_OK:
                        String resultMessage = result.getData().getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE);
                        if (resultMessage != null) {
                            Log.i("Proof of Payment", resultMessage);
                        }
                        Toast.makeText(getApplicationContext(), "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();


                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), "Canceled By User", Toast.LENGTH_SHORT).show();
                        break;
                    case EsewaPayment.RESULT_EXTRAS_INVALID:
                        resultMessage = result.getData().getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE);
                        if (resultMessage != null) {
                            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        );

        setupMainContent();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.menu_item_search_view);
        return true;
    }

    private void setupMainContent() {
        findViewById(R.id.close_fragment_icon).setOnClickListener(v -> {
            this.finish();
        });

        ImageView productImage = findViewById(R.id.product_iv);
        Glide.with(getApplicationContext())
                .load(product.getImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(productImage);

        TextView productPrice = findViewById(R.id.product_price_tv);
        productPrice.setText(String.format("%s per %s", product.getPrice(), product.getUnit()));

        TextView quantityTextView = findViewById(R.id.quantity_tv);

        ImageView quantityPlus = findViewById(R.id.quantity_plus);
        quantityPlus.setOnClickListener(v -> updateQuantity(quantityTextView, product.getUnitChange()));

        ImageView quantityMinus = findViewById(R.id.quantity_minus);
        quantityMinus.setOnClickListener(v -> updateQuantity(quantityTextView, -product.getUnitChange()));

        TextView productPreviousPrice = findViewById(R.id.product_prev_price);
        productPreviousPrice.setText(String.format("%s %s", product.getPreviousPrice(), product.getUnit()));
        productPreviousPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        TextView deliveryChargeTextView = findViewById(R.id.delivery_charge);
        double deliveryCharge = Double.parseDouble(deliveryChargeTextView.getText().toString().split(" ")[1]);

        TextView totalPriceTextView = findViewById(R.id.total_amount);
        totalPriceTextView.setText(String.format("%s", product.getPrice() + deliveryCharge));

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

        EditText deliveryLocationEditText = findViewById(R.id.delivery_location_et);

        ImageButton esewaButton = findViewById(R.id.button_esewa);
        esewaButton.setOnClickListener(v -> {
            double quantity = getQuantity(quantityTextView);
            String deliveryLocation = getDeliveryLocation(deliveryLocationEditText);
            if(deliveryLocation.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Delivery location is required", Toast.LENGTH_SHORT).show();
            } else {
                createOrder(quantity, deliveryLocation, deliveryCharge, PaymentMethod.ESEWA);
            }

        });

        ImageButton khaltiButton = findViewById(R.id.button_khalti);
        khaltiButton.setOnClickListener(v -> {
            double quantity = getQuantity(quantityTextView);
            String deliveryLocation = getDeliveryLocation(deliveryLocationEditText);
            if(deliveryLocation.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Delivery location is required", Toast.LENGTH_SHORT).show();
            } else {
                createOrder(quantity, deliveryLocation, deliveryCharge, PaymentMethod.KHALTI);
            }
        });
    }

    private String getDeliveryLocation(EditText deliveryLocationEditText) {
        return deliveryLocationEditText.getText().toString().trim();
    }

    private double getQuantity(TextView quantityTV) {
        return Double.parseDouble(quantityTV.getText().toString().trim().split(" ")[0]);
    }

    private String setupOrder(EditText deliveryLocation, TextView quantityTV) {
        double quantity = getQuantity(quantityTV);
        Log.d("Place Order button", "onViewCreated: " + quantity);
        String deliveryLoc = getDeliveryLocation(deliveryLocation);
        if(deliveryLoc.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Delivery location is required", Toast.LENGTH_SHORT).show();
            return "";
        }
        return String.format("%s %s", quantity, deliveryLoc);
    }

    private void setTotalPrice(TextView quantityTV, TextView priceTV, double deliveryCharge) {
        double quantity = Double.parseDouble(quantityTV.getText().toString().split(" ")[0]);
        double price = product.getPrice() * quantity;
        double priceWithDeliveryCharge = price + deliveryCharge;
        priceTV.setText(String.format("%s", priceWithDeliveryCharge));
    }

    private void updateQuantity(TextView quantityTV, double change) {
        //this logic should be moved to Product model itself.
        double quantity = Double.parseDouble(quantityTV.getText().toString().split(" ")[0]);
        quantity += change;
        if(quantity > 0 && quantity <= product.getStock())
            quantityTV.setText(String.format("%s %s", quantity, product.getUnit()));
        else if (quantity == 0) {
            Toast.makeText(getApplicationContext(), "Cannot order less than that", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Out of stock", Toast.LENGTH_SHORT).show();
        }
    }

    private void createOrder(double quantity, String deliveryLocation, double deliveryCharge, String paymentMethod) {
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
        Gson gson = RetrofitClient.getGson();
        Log.d("Place order", "createOrder: "+gson.toJson(order));

        //make payment to esewa
        if(paymentMethod.equals(PaymentMethod.ESEWA)) {
            //String callBackUrl =  String.format("%s/payments/esewa", RetrofitClient.BASE_URL);
            String callBackUrl = "https://6df2-2405-acc0-169-325d-512a-3994-40cd-14b1.ngrok-free.app/payments/esewa";
            Intent intent = EsewaPaymentGateway.makeEsewaPayment(BuyProductActivity.this, ""+order.getTotalPrice(), product.getName(), product.getId(), callBackUrl, new HashMap<>());
            registerActivity.launch(intent);
        } else if(paymentMethod.equals(PaymentMethod.KHALTI)) {
            final String[] khaltiPidx = {null};
            //call backend to get pidx
            KhaltiAPI khaltiAPI = RetrofitClient.getClient().create(KhaltiAPI.class);
            khaltiAPI.getPidx().enqueue(new Callback<KhaltiResponses.KhaltiPidxResponse>() {
                @Override
                public void onResponse(Call<KhaltiResponses.KhaltiPidxResponse> call, Response<KhaltiResponses.KhaltiPidxResponse> response) {
                    Log.d("TAG", "onResponse: Khalti "+ response.isSuccessful());
                    if(response.isSuccessful()) {
                        Log.d("TAG", "onResponse: "+gson.toJson(response.body()));
                        khaltiPidx[0] = response.body().getPidx();
                        Log.d("khalti response", "onResponse: khalti " + response.body().getPidx());
                    }

                    else Toast.makeText(getApplicationContext(), "Error getting pidx from server", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<KhaltiResponses.KhaltiPidxResponse> call, Throwable t) {
                    Log.d("TAG", "onFailure: "+t.getMessage());
                }
            });
            Log.d("Khalti", "createOrder: "+khaltiPidx[0]);
            KhaltiPaymentGateway.makeKhaltiPayment(getApplicationContext(), khaltiPidx[0]);
            Toast.makeText(getApplicationContext(), "Khalti Payment clicked", Toast.LENGTH_SHORT).show();
        }

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
}