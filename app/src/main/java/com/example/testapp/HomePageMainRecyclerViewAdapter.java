package com.example.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.example.testapp.models.HomePageModel;

public class HomePageMainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //views
    private static final int NORMAL_PRODUCT_LIST = 0;
    private static final int IMAGE_CAROUSEL = 1;
    private static final int POPULAR_PRODUCT_SLIDER = 2;

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
                view = inflater.inflate(R.layout.popular_listview, parent, false);
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
        if(position == 0){
            return IMAGE_CAROUSEL;
        } else if(position == 1) {
            return POPULAR_PRODUCT_SLIDER;
        } else {
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
                //load images
                Glide.with(homePageContext)
                        .load(homePageModel.getPopularProductsList().get(position).getImageUrl())
                        .centerCrop()
                        //.placeholder(R.drawable.loading_spinner)
                        .into(popularProductViewHolder.popularImage);

                //load titles
                popularProductViewHolder.productTitle.setText(homePageModel.getPopularProductsList().get(position).getImageTitle());

                //set click listener
                popularProductViewHolder.popularImage.setOnClickListener(view -> Toast.makeText(homePageContext, String.format("%s clicked", homePageModel.getPopularProductsList().get(position).getImageTitle()), Toast.LENGTH_SHORT).show());
                break;
            default:
                NormalProductViewHolder normalProductViewHolder = (NormalProductViewHolder) holder;
                //load images
                Glide.with(homePageContext)
                        .load(homePageModel.getNormalProductsList().get(position).getProductImage())
                        .centerCrop()
                        //.placeholder(R.drawable.loading_spinner)
                        .into(normalProductViewHolder.productImageView);

                normalProductViewHolder.productTitle.setText(homePageModel.getNormalProductsList().get(position).getProductName());
                normalProductViewHolder.productDescription.setText(homePageModel.getNormalProductsList().get(position).getProductDescription());
                normalProductViewHolder.productPrice.setText(String.format("%s", homePageModel.getNormalProductsList().get(position).getProductPrice()));
                normalProductViewHolder.productUnit.setText(homePageModel.getNormalProductsList().get(position).getProductUnit());
                normalProductViewHolder.productPreviousPrice.setText(String.format("%s", homePageModel.getNormalProductsList().get(position).getProductPreviousPrice()));
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

        ImageView popularImage;
        TextView productTitle;

        public PopularProductViewHolder(@NonNull View itemView) {
            super(itemView);

            popularImage = itemView.findViewById(R.id.popular_image_view);
            productTitle = itemView.findViewById(R.id.product_title);
        }
    }
    public static class NormalProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView productImageView;
        private final TextView productTitle;
        private final TextView productDescription;
        private final TextView productPrice;
        private final TextView productUnit;
        private final TextView productPreviousPrice;

        public NormalProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.product_image_view);
            productTitle = itemView.findViewById(R.id.product_title);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
            productUnit = itemView.findViewById(R.id.product_unit);
            productPreviousPrice = itemView.findViewById(R.id.product_previous_price);
        }
    }
}
