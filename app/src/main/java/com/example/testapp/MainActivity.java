package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.testapp.adapters.CategoriesRecyclerViewAdapter;
import com.example.testapp.adapters.HomePageMainRecyclerViewAdapter;
import com.example.testapp.interfaces.CategoryAPI;
import com.example.testapp.interfaces.ProductAPI;
import com.example.testapp.models.Category;
import com.example.testapp.models.HomePageModel;
import com.example.testapp.models.PopularProduct;
import com.example.testapp.models.Product;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.CategoryResponses;
import com.example.testapp.responses.ProductResponses;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {

    HomePageModel homePageModel;
    private final List<Category> categoryList = new ArrayList<>();
    private final List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup the toolbar
        activateToolbar(false);

        initCategoriesRecyclerView();
        drawMainRecyclerView();

    }

    private void drawMainRecyclerView() {
        List<SlideModel> dealsList = new ArrayList<>();

        dealsList.add(new SlideModel(RetrofitClient.BASE_URL+"/images/products/extra/mango1.jpg", ScaleTypes.CENTER_CROP));
        dealsList.add(new SlideModel(RetrofitClient.BASE_URL+"/images/products/extra/mango2.jpg", ScaleTypes.CENTER_CROP));
        dealsList.add(new SlideModel(RetrofitClient.BASE_URL+"/images/products/extra/mango3.jpg", ScaleTypes.CENTER_CROP));
        dealsList.add(new SlideModel(RetrofitClient.BASE_URL+"/images/products/extra/mango4.jpg", ScaleTypes.CENTER_CROP));


        List<PopularProduct> popularProducts = new ArrayList<>();
        popularProducts.add(new PopularProduct("https://bit.ly/2YoJ77H", "An Animal"));
        popularProducts.add(new PopularProduct("https://bit.ly/2BteuF2", "An Elephant"));
        popularProducts.add(new PopularProduct("https://bit.ly/3fLJf72", "Lol bruh"));
        popularProducts.add(new PopularProduct("https://bit.ly/2YoJ77H", "Another animal"));
        popularProducts.add(new PopularProduct("https://images.unsplash.com/photo-1520342868574-5fa3804e551c?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=6ff92caffcdd63681a35134a6770ed3b&auto=format&fit=crop&w=1951&q=80", "from flutter"));
        popularProducts.add(new PopularProduct("https://images.unsplash.com/photo-1522205408450-add114ad53fe?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=368f45b0888aeb0b7b08e3a1084d3ede&auto=format&fit=crop&w=1950&q=80", "An Animal"));
        popularProducts.add(new PopularProduct("https://images.unsplash.com/photo-1519125323398-675f0ddb6308?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=94a1e718d89ca60a6337a6008341ca50&auto=format&fit=crop&w=1950&q=80", "from flutter 3"));

        ProductAPI productAPI = RetrofitClient.getClient().create(ProductAPI.class);
        productAPI.getProducts().enqueue(new Callback<ProductResponses.MultipleProductResonse>() {
            @Override
            public void onResponse(Call<ProductResponses.MultipleProductResonse> call, Response<ProductResponses.MultipleProductResonse> response) {
                if(response.body() != null)
                    productList.addAll(response.body().getProducts());
            }

            @Override
            public void onFailure(Call<ProductResponses.MultipleProductResonse> call, Throwable t) {
                Log.e("ProductsGet", "Error while reading products", t);
            }
        });

        homePageModel = new HomePageModel(productList, popularProducts, dealsList);
        HomePageMainRecyclerViewAdapter adapter = new HomePageMainRecyclerViewAdapter(homePageModel, this);
        GridLayoutManager gridLayoutManager = getGridLayoutManager(adapter);
        RecyclerView productRecyclerView = findViewById(R.id.home_page_main_rv);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setAdapter(adapter);
    }

    @NonNull
    private GridLayoutManager getGridLayoutManager(HomePageMainRecyclerViewAdapter adapter) {
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
        return gridLayoutManager;
    }

    private void initCategoriesRecyclerView() {
        CategoriesRecyclerViewAdapter adapter = new CategoriesRecyclerViewAdapter(categoryList, this);


        Retrofit retrofit = RetrofitClient.getClient();
        CategoryAPI categoryAPI = retrofit.create(CategoryAPI.class);
        categoryAPI.getCategories().enqueue(new Callback<CategoryResponses.MultiCategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponses.MultiCategoryResponse> call, Response<CategoryResponses.MultiCategoryResponse> response) {
                if (response.body() != null) {
                    // Add "Home" category to the list at position 0
                    categoryList.add(0, new Category("home", "Home"));
                    adapter.notifyItemInserted(0);

                    // Add remaining categories from the response
                    int startPosition = 1; // after "Home" category
                    int itemCount = response.body().getCategories().size();
                    categoryList.addAll(startPosition, response.body().getCategories());
                    adapter.notifyItemRangeInserted(startPosition, itemCount);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponses.MultiCategoryResponse> call, Throwable t) {
                Log.d("category", "onFailure: "+t.getMessage());
            }
        });


        //init recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView categoriesRecyclerView = findViewById(R.id.categories_rv);
        categoriesRecyclerView.setLayoutManager(linearLayoutManager);
        categoriesRecyclerView.setAdapter(adapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle the item clicked
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_search_view) {
            SearchView searchView = (SearchView) item.getActionView();

            if (searchView != null) {
                searchView.setQueryHint("Search");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(query.isEmpty()) return false;

                        ArrayList<Product> filteredList = new ArrayList<>();
                        for(Product product : homePageModel.getNormalProductsList()){
                            if(product.getName().toLowerCase().contains(query.trim().toLowerCase())) {
                                filteredList.add(product);
                            }
                        }

                        Intent intent = new Intent(MainActivity.this, FilteredProductActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("filteredProductList", filteredList);
                        bundle.putString("SEARCH_QUERY", query.trim());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}