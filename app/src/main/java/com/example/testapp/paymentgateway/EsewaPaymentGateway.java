package com.example.testapp.paymentgateway;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.f1soft.esewapaymentsdk.EsewaConfiguration;
import com.f1soft.esewapaymentsdk.EsewaPayment;
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity;

import java.util.HashMap;

public class EsewaPaymentGateway {
    public static final String MERCHANT_ID = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R";
    private static final String MERCHANT_SECRET_KEY = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";
    public static final int REQUEST_CODE_PAYMENT = 1;
    private static EsewaConfiguration esewaConfiguration =null;

    public static EsewaConfiguration getEsewaConfiguration() {
        if(esewaConfiguration != null) return esewaConfiguration;
        esewaConfiguration = new EsewaConfiguration(MERCHANT_ID, MERCHANT_SECRET_KEY, EsewaConfiguration.ENVIRONMENT_TEST);
        return esewaConfiguration;
    }

    public static Intent makeEsewaPayment(Activity activity, String amount, String productName, String productId, String callbackUrl, HashMap<String, String> hashMap) {
        EsewaPayment eSewaPayment = new EsewaPayment(amount, productName, productId + System.nanoTime(), callbackUrl, hashMap);
        Intent intent = new Intent(activity, EsewaPaymentActivity.class);
        intent.putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, getEsewaConfiguration());
        intent.putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment);
        return intent;
    }
}
