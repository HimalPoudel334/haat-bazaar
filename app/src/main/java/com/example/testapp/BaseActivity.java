package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle the item clicked
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) finish();
        if (itemId == R.id.menu_item_cart) {
            Toast.makeText(getApplicationContext(), "Cart clicked", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(BaseActivity.this, CartActivity.class);
            // intent.putExtra("customerId", "customerIdHere");
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}