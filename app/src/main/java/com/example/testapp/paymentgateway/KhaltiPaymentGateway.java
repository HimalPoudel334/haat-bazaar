package com.example.testapp.paymentgateway;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.testapp.interfaces.KhaltiAPI;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.models.KhaltiPayment;
import com.example.testapp.network.RetrofitClient;
import com.google.gson.JsonElement;
import com.khalti.checkout.Khalti;
import com.khalti.checkout.data.Environment;
import com.khalti.checkout.data.KhaltiPayConfig;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class KhaltiPaymentGateway {
    public static final String TEST_SECRET_KEY = "test_secret_key_471495ae440c4ee1809f7ae9720da619";
    public static final String TEST_PUBLIC_KEY = "test_public_key_8d2d69b38a0242e7a3f1c25123673767";

    public static final String LIVE_SECRET_KEY = "6c30a9d14b5043149df573925be9c52a";
    public static final String LIVE_PUBLIC_KEY = "3f6fe11cefbd499faa0de6d68d76785b";

    private static KhaltiPayConfig config = null;

    public static KhaltiPayConfig getKhaltiPayConfig(String pidx) {
        if(config != null) return config;
        config = new KhaltiPayConfig(String.format("live_public_key_%s", LIVE_PUBLIC_KEY), pidx, true, Environment.TEST);
        return config;
    }

    public static Khalti makeKhaltiPayment(Context context, String pidx) {
        Log.d("TAG", "makeKhaltiPayment: "+pidx);
        return Khalti.Companion.init(context, getKhaltiPayConfig(pidx),
            (paymentResult, khalti) -> {
                Log.i("Demo | onPaymentResult", paymentResult.toString());

                //call backed api to once again confirm payment and create payment
                confirmPayment(paymentResult.getPayload().getPidx(), paymentResult.getPayload().getPurchaseOrderId()); //purchaseOrderId is actually OrderId

                khalti.close();
                Toast.makeText(context, "Khalti Payment Successful", Toast.LENGTH_SHORT).show();
            },
            (payload, khalti) -> {
                String logMessage = "Demo | onMessage " +
                        payload.getEvent() +
                        (payload.getCode() != null ? " (" + payload.getCode() + ")" : "") +
                        " | " +
                        payload.getMessage() +
                        " | " +
                        payload.getNeedsPaymentConfirmation();

                Log.i("Demo | onMessage", logMessage);
                khalti.close();
                Toast.makeText(context, "onMessage Khalti Payment failed: " + logMessage, Toast.LENGTH_LONG).show();
            },
            khalti -> {
                Log.i("Demo | onReturn", "OnReturn");
                Toast.makeText(context, "onReturn Khalti Payment failed: ", Toast.LENGTH_SHORT).show();
            }
        );
    }

    private static void confirmPayment(String pidx, String orderId) {
        KhaltiPayment.KhaltiPaymentConfirmPayload payload = new KhaltiPayment.KhaltiPaymentConfirmPayload(pidx, orderId);
        KhaltiAPI khaltiAPI = RetrofitClient.getAuthClient(AuthManager.getInstance().getToken()).create(KhaltiAPI.class);
        khaltiAPI.verifyPayment(payload).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(!response.isSuccessful()) {
                    try {
                        Log.d("Payment verification", "onResponse: creating payment eror: " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }

}
