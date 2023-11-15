package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.testapp.adapters.CategoriesRecyclerViewAdapter;
import com.example.testapp.adapters.HomePageMainRecyclerViewAdapter;
import com.example.testapp.models.HomePageModel;
import com.example.testapp.models.PopularProduct;
import com.example.testapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    HomePageModel homePageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        drawMainRecyclerView();
        initCategoriesRecyclerView();

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
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Vindi",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTf6klA8SgPsjiv7mq-0qkzJQoI6Rkf8YeA3w&usqp=CAU", "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1

                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Iskus",
                        "https://4.bp.blogspot.com/-qXf8K4bD1Pc/U2qRMlOz2nI/AAAAAAAAS_4/bO9h-6m_RQk/s1600/1-DSC00457.JPG", "Local bodi without chemicals",
                        20.0,
                        30.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Iskus",
                        "https://4.bp.blogspot.com/-qXf8K4bD1Pc/U2qRMlOz2nI/AAAAAAAAS_4/bO9h-6m_RQk/s1600/1-DSC00457.JPG", "Local bodi without chemicals",
                        20.0,
                        30.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Kharbuza",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsIzAQLsz1qKhCFfCDfeBwLOQwfrnThFu5Qg&usqp=CAU",
                        "Local kharbuza without chemicals",
                        90.0,
                        120.0,
                        "kg",
                        0.25,
                        5
                )
        );
        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Baigun",
                        "https://t4.ftcdn.net/jpg/03/69/98/49/360_F_369984944_mp5bcE534T45okjhprMta7z6ujQlaYXC.jpg",
                        "Local bodi without chemicals",
                        60.0,
                        80.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals.",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Bodi",
                        "https://khetifood.com/image/cache/catalog/kheti_bodi-500x500.jpg",
                        "Local bodi without chemicals",
                        120.0,
                        150.0,
                        "kg",
                        0.25,
                        1
                )
        );

        productList.add(new Product(
                        "Iskus",
                        "https://4.bp.blogspot.com/-qXf8K4bD1Pc/U2qRMlOz2nI/AAAAAAAAS_4/bO9h-6m_RQk/s1600/1-DSC00457.JPG", "Local bodi without chemicals",
                        20.0,
                        30.0,
                        "kg",
                        0.25,
                        1
                )
        );

        homePageModel = new HomePageModel(productList, popularProducts, dealsList);
        //init recycler view
        HomePageMainRecyclerViewAdapter adapter = new HomePageMainRecyclerViewAdapter(homePageModel, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case HomePageMainRecyclerViewAdapter.IMAGE_CAROUSEL:
                    case HomePageMainRecyclerViewAdapter.POPULAR_PRODUCT_SLIDER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        RecyclerView productRecyclerView = findViewById(R.id.home_page_main_rv);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setAdapter(adapter);
    }

    private void initCategoriesRecyclerView() {
        List<String> categoriesList = new ArrayList<>();
        categoriesList.add("Home");

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle the item clicked
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_search_view) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Search");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(query.length() == 0 || query == null) return false;

                    ArrayList<Product> filteredList = new ArrayList<>();
                    for(Product product : homePageModel.getNormalProductsList()){
                        if(product.getProductName().toLowerCase().contains(query.trim().toLowerCase())) {
                            filteredList.add(product);
                        }
                    }

                    //later when connected to api, we will just pass the string query to new intent and
                    // will call the api with that query on the activity itself.
                    //be sure to remove the Parcelable interface from Product model.
                    Intent intent = new Intent(MainActivity.this, FilteredProductActivity.class);
                    //Create the bundle
                    Bundle bundle = new Bundle();
                    //Add your data to bundle
                    bundle.putParcelableArrayList("filteredProductList", filteredList);
                    bundle.putString("SEARCH_QUERY", query.trim());
                    //Add the bundle to the intent
                    intent.putExtras(bundle);
                    //Fire that second activity
                    startActivity(intent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}