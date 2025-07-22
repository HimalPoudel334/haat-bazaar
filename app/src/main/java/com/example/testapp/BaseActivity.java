package com.example.testapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.testapp.helpers.FcmRegistrationTokenApiHelper;
import com.example.testapp.managers.AuthManager;
import com.example.testapp.models.User;
import com.example.testapp.network.RetrofitClient;
import com.google.firebase.messaging.FirebaseMessaging;

public class BaseActivity extends AppCompatActivity {

    private FcmRegistrationTokenApiHelper fcmApiHelper;
    private static final String TAG = "BaseActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize AuthManager and get current user
        AuthManager.init(this);


        // Check and request POST_NOTIFICATIONS permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // TIRAMISU is API 33
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
                Log.d(TAG, "POST_NOTIFICATIONS permission already granted.");
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // Explain to the user why the permission is needed (optional, good UX)
                // For simplicity, directly request for now, but in production, show a dialog first.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                // Directly request the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }


        // Get the FCM registration token if the current user is admin
        if(getCurrentUser().isAdmin()) {
            fcmApiHelper = new FcmRegistrationTokenApiHelper();

            FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    String prevToken = AuthManager.getInstance().getKeyFcmToken();
                    if(token != null && prevToken != null && !prevToken.equalsIgnoreCase(token)) {
                        fcmApiHelper.sendRegistrationTokenToBackend(getCurrentUserId(), token);
                        AuthManager.getInstance().setKeyFcmToken(token);
                    }
                    Log.d(TAG, "FCM Token: " + token);

                });
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. You can proceed with showing notifications.
                    Log.d(TAG, "POST_NOTIFICATIONS permission granted.");
                    // You might want to re-check if there are pending notifications to show
                } else {
                    // Permission is denied. You should inform the user why notifications are important.
                    Log.w(TAG, "POST_NOTIFICATIONS permission denied.");
                    // Optionally, show a dialog explaining why permission is needed and how to enable it from settings.
                }
            });

    public void activateToolbar(boolean enableHome, String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
            if (title != null) {
                actionBar.setTitle(title);
            }
        }
    }


    public boolean isUserLoggedIn() {
        return AuthManager.getInstance().getCurrentUser().isLoggedIn();
    }

    public boolean isUserAdmin() {
        return AuthManager.getInstance().getCurrentUser().isAdmin();
    }

    public User getCurrentUser() {
        return AuthManager.getInstance().getCurrentUser();
    }

    public String getUserToken() {
        return AuthManager.getInstance().getAccessToken();
    }
    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem profileMenu = menu.findItem(R.id.menu_item_profile);
        if (profileMenu != null) {
            profileMenu.setVisible(isUserLoggedIn());
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == android.R.id.home) finish();
        if (itemId == R.id.menu_item_cart) {
            Intent intent = new Intent(BaseActivity.this, CartActivity.class);;
            if(!isUserLoggedIn()) {
                intent = new Intent(BaseActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            return true;
        }
        if(itemId == R.id.menu_item_profile){
            if(isUserLoggedIn()) {
                Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
                if(isUserAdmin())
                    intent = new Intent(BaseActivity.this, AdminPanelActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu(); // Ensures menu updates when activity comes back to foreground
    }
}