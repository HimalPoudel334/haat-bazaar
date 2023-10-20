package com.example.testapp.models;

import com.denzcoskun.imageslider.models.SlideModel;

import java.util.List;

public class HomePageModel {
    private List<Product> normalProductsList;
    private List<PopularProduct> popularProductsList;
    private List<SlideModel> bestDealsList;

    public HomePageModel(List<Product> normalProductsList, List<PopularProduct> popularProductsList, List<SlideModel> bestDealsList) {
        this.normalProductsList = normalProductsList;
        this.popularProductsList = popularProductsList;
        this.bestDealsList = bestDealsList;
    }

    public List<Product> getNormalProductsList() {
        return normalProductsList;
    }

    public void setNormalProductsList(List<Product> normalProductsList) {
        this.normalProductsList = normalProductsList;
    }

    public List<PopularProduct> getPopularProductsList() {
        return popularProductsList;
    }

    public void setPopularProductsList(List<PopularProduct> popularProductsList) {
        this.popularProductsList = popularProductsList;
    }

    public List<SlideModel> getBestDealsList() {
        return bestDealsList;
    }

    public void setBestDealsList(List<SlideModel> bestDealsList) {
        this.bestDealsList = bestDealsList;
    }
}
