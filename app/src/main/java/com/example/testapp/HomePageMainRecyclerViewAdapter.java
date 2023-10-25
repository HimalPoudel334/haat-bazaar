package com.example.testapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.example.testapp.models.HomePageModel;
import com.example.testapp.models.Product;

public class HomePageMainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //views
    public static final int NORMAL_PRODUCT_LIST = 0;
    public static final int IMAGE_CAROUSEL = 1;
    public static final int POPULAR_PRODUCT_SLIDER = 2;

    private final HomePageModel homePageModel;
    private final Context homePageContext;

    public HomePageMainRecyclerViewAdapter(HomePageModel homePageModel, Context homePageContext) {
        this.homePageModel = homePageModel;
        this.homePageContext = homePageContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case IMAGE_CAROUSEL:
                view = inflater.inflate(R.layout.image_slider_view, parent, false);
                viewHolder = new CarouselViewHolder(view);
                break;

            case POPULAR_PRODUCT_SLIDER:
                view = inflater.inflate(R.layout.popular_products_rv, parent, false);
                viewHolder = new PopularProductViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.product_item, parent, false);
                viewHolder = new NormalProductViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return IMAGE_CAROUSEL;
            case 1:
                return POPULAR_PRODUCT_SLIDER;
            default:
                return NORMAL_PRODUCT_LIST;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case IMAGE_CAROUSEL:
                CarouselViewHolder carouselViewHolder = (CarouselViewHolder) holder;
                carouselViewHolder.imageSlider.setImageList(homePageModel.getBestDealsList());
                break;

            case POPULAR_PRODUCT_SLIDER:
                PopularProductViewHolder popularProductViewHolder = (PopularProductViewHolder) holder;

                PopularRecyclerViewAdapter popularAdapter = new PopularRecyclerViewAdapter(homePageContext, homePageModel.getPopularProductsList());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homePageContext, LinearLayoutManager.HORIZONTAL, false);
                popularProductViewHolder.popularProductRecyclerView.setLayoutManager(linearLayoutManager);
                popularProductViewHolder.popularProductRecyclerView.setAdapter(popularAdapter);
                break;

            default:
                NormalProductViewHolder normalProductViewHolder = (NormalProductViewHolder) holder;
                //get current normal product
                Product normalProduct = homePageModel.getNormalProductsList().get(position);
                //load images
                Glide.with(homePageContext)
                        .load(normalProduct.getProductImage())
                        .centerCrop()
                        //.placeholder(R.drawable.loading_spinner)
                        .into(normalProductViewHolder.productImageView);

                normalProductViewHolder.productTitle.setText(normalProduct.getProductName());
                normalProductViewHolder.productDescription.setText(normalProduct.getProductDescription());
                normalProductViewHolder.productPrice.setText(String.format("%s per %s", normalProduct.getProductPrice(), normalProduct.getProductUnit()));
                normalProductViewHolder.productPreviousPrice.setText(String.format("%s per %s", normalProduct.getProductPreviousPrice(), normalProduct.getProductUnit()));
                normalProductViewHolder.productPreviousPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModel.getNormalProductsList().size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageSlider imageSlider;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSlider = itemView.findViewById(R.id.image_slider);
        }
    }

    public static class PopularProductViewHolder extends RecyclerView.ViewHolder {
        RecyclerView popularProductRecyclerView;

        public PopularProductViewHolder(@NonNull View itemView) {
            super(itemView);

            popularProductRecyclerView = itemView.findViewById(R.id.popular_products_rv);

        }
    }

    public static class NormalProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView productImageView;
        private final TextView productTitle;
        private final TextView productDescription;
        private final TextView productPrice;
        private final TextView productPreviousPrice;

        public ImageView getProductImageView() {
            return productImageView;
        }

        public TextView getProductTitle() {
            return productTitle;
        }

        public TextView getProductDescription() {
            return productDescription;
        }

        public TextView getProductPrice() {
            return productPrice;
        }

        public TextView getProductPreviousPrice() {
            return productPreviousPrice;
        }

        public NormalProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.product_image_view);
            productTitle = itemView.findViewById(R.id.product_title);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
            productPreviousPrice = itemView.findViewById(R.id.product_previous_price);
        }
    }
}
