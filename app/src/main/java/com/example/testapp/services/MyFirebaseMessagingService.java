package com.example.testapp.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.testapp.R;
import com.example.testapp.helpers.FcmRegistrationTokenApiHelper;
import com.example.testapp.managers.AuthManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "admin_order_channel";

    private FcmRegistrationTokenApiHelper fcmApiHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        fcmApiHelper = new FcmRegistrationTokenApiHelper();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        fcmApiHelper.sendRegistrationTokenToBackend(AuthManager.getInstance().getCurrentUser().getId(), token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Always check data payload first, as it's reliable for custom info
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String orderId = data.get("order_id");
            String customerName = data.get("customer_name");
            String totalAmount = data.get("total_amount");
            String eventType = data.get("event_type");

            if ("new_order".equals(eventType) && orderId != null) {
                String title = "New Order: " + orderId;
                String body = "Customer: " + customerName + ", Total: Rs. " + totalAmount;

                // --- NEW: Pass orderId to showNotification ---
                showNotification(orderId.hashCode(), title, body);
            } else {
                // Fallback for other data messages without specific orderId
                showNotification(0, "New Message", "Data received: " + data.toString());
            }
        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // If the message is a notification-only message (no data payload with order_id)
            // you'll need a different strategy for a unique ID, e.g., a simple counter
            // or a static ID if they are generic notifications that can be replaced.
            showNotification(1, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody()); // Use a different static ID or counter
        }
    }

    /**
     * Creates and displays a system notification.
     * @param notificationId A unique integer ID for this notification.
     * @param title The title of the notification.
     * @param message The body text of the notification.
     */
    private void showNotification(int notificationId, String title, String message) {
        // --- Permission Check ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "Notification permission not granted, cannot display notification (ID: " + notificationId + ").");
                return;
            }
        }

        // --- Notification Channel (always required for Android 8.0+) ---
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        // --- Build and Display Notification ---
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.haatbazaar)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Automatically dismisses the notification when clicked

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        // --- NEW: Use the passed notificationId ---
        notificationManagerCompat.notify(notificationId, builder.build());
    }
}