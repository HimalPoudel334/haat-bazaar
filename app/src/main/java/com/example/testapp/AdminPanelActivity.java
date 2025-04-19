package com.example.testapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.adapters.EntityAdapter;
import com.example.testapp.models.Entity;

import java.util.ArrayList;
import java.util.List;

public class AdminPanelActivity extends AppCompatActivity {

    private RecyclerView entityRecyclerView;
    private EntityAdapter entityAdapter;
    private List<Entity> entityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        entityRecyclerView = findViewById(R.id.entity_recycler_view);

        // Define your entity data
        entityList = new ArrayList<>();
        entityList.add(new Entity("Products", R.drawable.ic_launcher_background));
        entityList.add(new Entity("Users", R.drawable.ic_launcher_background));
        entityList.add(new Entity("Orders", R.drawable.ic_launcher_background));
        entityList.add(new Entity("Inventory", R.drawable.ic_launcher_background));
        entityList.add(new Entity("Users", R.drawable.ic_launcher_background));
        entityList.add(new Entity("Reports", R.drawable.ic_launcher_background));
        entityList.add(new Entity("Settings", R.drawable.ic_launcher_background));
        // Add more entities as needed

        // Create the adapter
        entityAdapter = new EntityAdapter(this, entityList);

        // Set the layout manager (GridLayoutManager for a grid view)
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // 2 columns
        entityRecyclerView.setLayoutManager(layoutManager);

        // Set the adapter to the RecyclerView
        entityRecyclerView.setAdapter(entityAdapter);
    }
}