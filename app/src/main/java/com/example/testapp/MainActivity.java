package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.testapp.models.HomePageModel;
import com.example.testapp.models.PopularProduct;
import com.example.testapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawMainRecyclerView();
        initCategoriesRecyclerView();
        /*drawImageCarousel();
        initPopularRecyclerView();
        initProductRecyclerView(); */
    }

    private void drawMainRecyclerView() {
        //create the vars
        //carousel
        List<SlideModel> dealsList = new ArrayList<>(); // Create image list
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        dealsList.add(new SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.FIT));
        dealsList.add(new SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct.", ScaleTypes.FIT));
        dealsList.add(new SlideModel("https://bit.ly/3fLJf72", "And people do that.", ScaleTypes.FIT));

        //popular products
        List<PopularProduct> popularProducts = new ArrayList<>();
        popularProducts.add(new PopularProduct("https://bit.ly/2YoJ77H", "An Animal"));
        popularProducts.add(new PopularProduct("https://bit.ly/2BteuF2", "An Elephant"));
        popularProducts.add(new PopularProduct("https://bit.ly/3fLJf72", "Lol bruh"));
        popularProducts.add(new PopularProduct("https://bit.ly/2YoJ77H", "Another animal"));
        popularProducts.add(new PopularProduct("https://images.unsplash.com/photo-1520342868574-5fa3804e551c?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=6ff92caffcdd63681a35134a6770ed3b&auto=format&fit=crop&w=1951&q=80", "from flutter"));
        popularProducts.add(new PopularProduct("https://images.unsplash.com/photo-1522205408450-add114ad53fe?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=368f45b0888aeb0b7b08e3a1084d3ede&auto=format&fit=crop&w=1950&q=80", "An Animal"));
        popularProducts.add(new PopularProduct("https://images.unsplash.com/photo-1519125323398-675f0ddb6308?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=94a1e718d89ca60a6337a6008341ca50&auto=format&fit=crop&w=1950&q=80", "from flutter 3"));

        //normal products
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Kharbuza",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsIzAQLsz1qKhCFfCDfeBwLOQwfrnThFu5Qg&usqp=CAU",
                        "Local kharbuza without chemicals",
                        90.0,
                        120.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Vindi",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTf6klA8SgPsjiv7mq-0qkzJQoI6Rkf8YeA3w&usqp=CAU", "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Iskus",
                        "https://4.bp.blogspot.com/-qXf8K4bD1Pc/U2qRMlOz2nI/AAAAAAAAS_4/bO9h-6m_RQk/s1600/1-DSC00457.JPG", "Local bodi without chemicals",
                        20.0,
                        30.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Iskus",
                        "https://4.bp.blogspot.com/-qXf8K4bD1Pc/U2qRMlOz2nI/AAAAAAAAS_4/bO9h-6m_RQk/s1600/1-DSC00457.JPG", "Local bodi without chemicals",
                        20.0,
                        30.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        HomePageModel homePageModel = new HomePageModel(productList, popularProducts, dealsList);
        //init recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView productRecyclerView = findViewById(R.id.home_page_main_rv);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        HomePageMainRecyclerViewAdapter adapter = new HomePageMainRecyclerViewAdapter(homePageModel, this);
        productRecyclerView.setAdapter(adapter);
    }

    private void initCategoriesRecyclerView() {
        List<String> categoriesList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            categoriesList.add("Category " + i);
        }

        //init recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView categoriesRecyclerView = findViewById(R.id.categories_rv);
        categoriesRecyclerView.setLayoutManager(linearLayoutManager);
        CategoriesRecyclerViewAdapter adapter = new CategoriesRecyclerViewAdapter(categoriesList, this);
        categoriesRecyclerView.setAdapter(adapter);

    }

    /*private void drawImageCarousel() {
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
        *//*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView popularRecyclerView = findViewById(R.id.popularRecyclerView);
        popularRecyclerView.setLayoutManager(linearLayoutManager);
        PopularRecyclerViewAdapter adapter = new PopularRecyclerViewAdapter(this, popularImageUrls, popularImageTitles);
        popularRecyclerView.setAdapter(adapter);*//*

    }

    private void initProductRecyclerView() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Kharbuza",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsIzAQLsz1qKhCFfCDfeBwLOQwfrnThFu5Qg&usqp=CAU",
                        "Local kharbuza without chemicals",
                        90.0,
                        120.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Vindi",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTf6klA8SgPsjiv7mq-0qkzJQoI6Rkf8YeA3w&usqp=CAU", "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Iskus",
                        "https://4.bp.blogspot.com/-qXf8K4bD1Pc/U2qRMlOz2nI/AAAAAAAAS_4/bO9h-6m_RQk/s1600/1-DSC00457.JPG", "Local bodi without chemicals",
                        20.0,
                        30.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Iskus",
                        "https://4.bp.blogspot.com/-qXf8K4bD1Pc/U2qRMlOz2nI/AAAAAAAAS_4/bO9h-6m_RQk/s1600/1-DSC00457.JPG", "Local bodi without chemicals",
                        20.0,
                        30.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg"
                )
        );

        //init recycler view
        *//*GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView productRecyclerView = findViewById(R.id.product_recycler_view);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        ProductRecyclerViewAdapter adapter = new ProductRecyclerViewAdapter(this, productList);
        productRecyclerView.setAdapter(adapter);*//*

    }
*/
}