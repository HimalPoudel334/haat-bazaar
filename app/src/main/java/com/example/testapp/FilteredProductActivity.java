package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.testapp.adapters.FilteredProductRecyclerViewAdapter;
import com.example.testapp.models.Product;

import java.util.ArrayList;

public class FilteredProductActivity extends BaseActivity {

    private ArrayList<Product> filteredProductList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_product);

        filteredProductList = getIntent().getExtras().getParcelableArrayList("filteredProductList");

        //setup toolbar
        activateToolbar(true);

        showFilteredProductRecyclerView();
    }

    private void showFilteredProductRecyclerView() {
        //init recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView filteredProductRecyclerView = findViewById(R.id.filtered_product_rv);
        filteredProductRecyclerView.setLayoutManager(linearLayoutManager);
        FilteredProductRecyclerViewAdapter adapter = new FilteredProductRecyclerViewAdapter(this, filteredProductList);
        filteredProductRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_item_search_view);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setIconified(false);
        searchMenuItem.expandActionView();
        searchView.clearFocus();
        ///AT THE END YOU NEED TO SET THE TEXT
        String searchQuery = getIntent().getStringExtra("SEARCH_QUERY");
        searchView.setQuery(searchQuery, false);
        return true;

    }
}