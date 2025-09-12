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

    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Always check data payload first, as it's reliable for custom info
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String orderId = data.get("order_id");
            String customerName = data.get("customer_name");
            String totalAmount = data.get("total_amount");
            String eventType = data.get("event_type"); // This is crucial!
            String messageTitle = data.get("title");
            String messageBody = data.get("body");

            int notificationId = 0;
            if (orderId != null && !orderId.isEmpty()) {
                notificationId = orderId.hashCode();
            } else {

                notificationId = eventType != null ? eventType.hashCode() : 0;
            }


            switch (eventType) {
                case "new_order":
                    if (orderId != null) {
                        String title = "New Order: " + orderId;
                        String body = "Customer: " + customerName + ", Total: Rs. " + totalAmount;
                        showNotification(notificationId, title, body);
                    }
                    break;

                case "payment_received":
                    // Assuming payment_received also sends relevant data like orderId or transactionId
                    String paymentAmount = data.get("amount");
                    String transactionId = data.get("transaction_id");
                    String paymentMethod = data.get("payment_method");

                    String titlePayment = "Payment Received!";
                    StringBuilder bodyBuilder = new StringBuilder();
                    bodyBuilder.append("Amount: Rs. ").append(paymentAmount);

                    if (orderId != null && !orderId.isEmpty()) {
                        bodyBuilder.append(" for Order: ").append(orderId);
                    }
                    if (transactionId != null && !transactionId.isEmpty()) {
                        bodyBuilder.append(" (Txn: ").append(transactionId).append(")");
                    }
                    if (paymentMethod != null && !paymentMethod.isEmpty()) {
                        bodyBuilder.append(" via ").append(paymentMethod);
                    }

                    String bodyPayment = bodyBuilder.toString();
                    showNotification(notificationId, titlePayment, bodyPayment);
                    break;

                case "order_cancelled":
                    String cancelReason = data.get("reason");
                    String titleCancelled = "Order Cancelled: " + orderId;
                    String bodyCancelled = "Reason: " + (cancelReason != null ? cancelReason : "N/A");
                    showNotification(notificationId, titleCancelled, bodyCancelled);
                    break;

                case "order_fulfilled":
                    String fulfillDetails = data.get("details"); // e.g., "delivered", "picked up"
                    String titleFulfilled = "Order Fulfilled: " + orderId;
                    String bodyFulfilled = fulfillDetails != null ? "Status: " + fulfillDetails : "Your order has been completed.";
                    showNotification(notificationId, titleFulfilled, bodyFulfilled);
                    break;

                default:
                    // Fallback for any other data messages not specifically handled above
                    // or if 'title' and 'body' are sent directly in the data payload
                    String defaultTitle = messageTitle != null ? messageTitle : "New Message";
                    String defaultBody = messageBody != null ? messageBody : "Data received: " + data.toString();
                    showNotification(notificationId, defaultTitle, defaultBody);
                    break;
            }

        } else if (remoteMessage.getNotification() != null) {
            // This block handles "notification-only" messages sent from Firebase Console
            // or messages where the server only sends the 'notification' payload.
            // These typically don't have custom data like 'eventType'.
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            // For generic notifications without a data payload, you often have to rely on
            // a static ID or a very simple counter if you don't mind them overwriting each other,
            // or if they are truly unique and you want them to stack.
            // Using a static ID (e.g., 1) will replace the previous notification with the same ID.
            // If you want them to stack, you'd need a dynamic ID (e.g., System.currentTimeMillis() or a counter).
            showNotification(1, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
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