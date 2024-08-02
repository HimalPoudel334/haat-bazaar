package com.example.testapp.paymentgateway;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.khalti.checkout.Khalti;
import com.khalti.checkout.data.Environment;
import com.khalti.checkout.data.KhaltiPayConfig;

public class KhaltiPaymentGateway {
    public static final String TEST_SECRET_KEY = "test_secret_key_471495ae440c4ee1809f7ae9720da619";
    public static final String TEST_PUBLIC_KEY = "test_public_key_8d2d69b38a0242e7a3f1c25123673767";

    public static final String LIVE_SECRET_KEY = "6c30a9d14b5043149df573925be9c52a";
    public static final String LIVE_PUBLIC_KEY = "3f6fe11cefbd499faa0de6d68d76785b";

    private static KhaltiPayConfig config = null;

    private static KhaltiPayConfig getKhaltiPayConfig(String pidx) {
        if(config != null) return config;
        config = new KhaltiPayConfig(LIVE_PUBLIC_KEY, pidx, true, Environment.TEST);
        return config;
    }

    public static void makeKhaltiPayment(Context context, String pidx) {
        Khalti.Companion.init(context, getKhaltiPayConfig(pidx),
            (paymentResult, khalti) -> {
                Log.i("Demo | onPaymentResult", paymentResult.toString());
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
                Toast.makeText(context, "Khalti Payment failed: " + payload.getMessage(), Toast.LENGTH_SHORT).show();
            },
            khalti -> {
                Log.i("Demo | onReturn", "OnReturn");
            }
        );
    }

}
