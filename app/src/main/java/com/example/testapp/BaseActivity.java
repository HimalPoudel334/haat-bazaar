package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
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
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize AuthManager and get current user
        AuthManager.init(this);
        currentUser = AuthManager.getInstance().getCurrentUser();
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
        return currentUser.isLoggedIn();
    }

    public boolean isUserAdmin() {
        return currentUser.isAdmin();
    }

    public String getUserToken() {
        return AuthManager.getInstance().getToken();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem profileMenu = menu.findItem(R.id.menu_item_profile);
        if(isUserAdmin()) {
            profileMenu.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == android.R.id.home) finish();
        if (itemId == R.id.menu_item_cart) {
            Intent intent;
            if(currentUser.isLoggedIn()) {
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
            Intent intent;
            if(isUserAdmin())
                intent = new Intent(BaseActivity.this, ProfileActivity.class);
            else
                intent = new Intent(BaseActivity.this, AdminPanelActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}