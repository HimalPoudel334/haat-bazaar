package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.testapp.adapters.FilteredProductRecyclerViewAdapter;
import com.example.testapp.models.Product;

import java.util.ArrayList;

public class FilteredProductActivity extends AppCompatActivity {

    private ArrayList<Product> filteredProductList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_product);

        filteredProductList = getIntent().getExtras().getParcelableArrayList("filteredProductList");

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

}