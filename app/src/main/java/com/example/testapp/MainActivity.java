package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCategoriesRecyclerView();
        drawImageCarousel();
        initPopularRecyclerView();
    }

    private void drawImageCarousel() {
        List<SlideModel> imageList = new ArrayList<>(); // Create image list

        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        imageList.add(new SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct.", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://bit.ly/3fLJf72", "And people do that.", ScaleTypes.FIT));

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        imageSlider.setImageList(imageList);
    }

    private void initPopularRecyclerView() {
        List<String> popularImageUrls = new ArrayList<>();
        List<String> popularImageTitles = new ArrayList<>();

        popularImageUrls.add("https://bit.ly/2YoJ77H");
        popularImageTitles.add("An Animal");

        popularImageUrls.add("https://bit.ly/2BteuF2");
        popularImageTitles.add("Elephants");

        popularImageUrls.add("https://bit.ly/3fLJf72");
        popularImageTitles.add("And people do that.");


        popularImageUrls.add("https://bit.ly/2YoJ77H");
        popularImageTitles.add("And people do that.");

        popularImageUrls.add("https://images.unsplash.com/photo-1520342868574-5fa3804e551c?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=6ff92caffcdd63681a35134a6770ed3b&auto=format&fit=crop&w=1951&q=80");
        popularImageTitles.add("from flutter1");

        popularImageUrls.add("https://images.unsplash.com/photo-1522205408450-add114ad53fe?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=368f45b0888aeb0b7b08e3a1084d3ede&auto=format&fit=crop&w=1950&q=80");
        popularImageTitles.add("from flutter2");

        popularImageUrls.add("https://images.unsplash.com/photo-1519125323398-675f0ddb6308?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=94a1e718d89ca60a6337a6008341ca50&auto=format&fit=crop&w=1950&q=80");
        popularImageTitles.add("from flutter3");

        //init recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView popularRecyclerView = findViewById(R.id.popularRecyclerView);
        popularRecyclerView.setLayoutManager(linearLayoutManager);
        PopularRecyclerViewAdapter adapter = new PopularRecyclerViewAdapter(this, popularImageUrls, popularImageTitles);
        popularRecyclerView.setAdapter(adapter);

    }

    private void initCategoriesRecyclerView() {
        List<String> categoriesList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            categoriesList.add("Category " + i);
        }

        //init recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView categoriesRecyclerView = findViewById(R.id.categories_rv);
        categoriesRecyclerView.setLayoutManager(linearLayoutManager);
        CategoriesRecyclerViewAdapter adapter = new CategoriesRecyclerViewAdapter(categoriesList, this);
        categoriesRecyclerView.setAdapter(adapter);

    }
}