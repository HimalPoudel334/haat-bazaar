package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testapp.managers.AuthManager;
import com.example.testapp.models.User;
import com.example.testapp.network.RetrofitClient;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize AuthManager and get current user
        AuthManager.init(this);
    }

    public void activateToolbar(boolean enableHome) {
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
        return AuthManager.getInstance().getToken();
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
            Intent intent;
            if(isUserLoggedIn()) {
                intent = new Intent(BaseActivity.this, CartActivity.class);
                startActivity(intent);
            }
            else {
                intent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            return true;
        }
        if(itemId == R.id.menu_item_profile){
            if(isUserLoggedIn()) {
                Intent intent;
                if(isUserAdmin())
                    intent = new Intent(BaseActivity.this, ProfileActivity.class);
                else
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