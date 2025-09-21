package com.example.testapp.responses;

import com.example.testapp.models.ProductRating;

import java.util.List;

public class ProductRatingResponses {

    public static class MultiProductRatingResponse {
        private List<ProductRating> ratings;

        public List<ProductRating> getProductRatings() {
            return ratings;
        }
    }

    public static class SingleProductRatingResponse {
        private ProductRating rating;

        public ProductRating getProductRating() {
            return rating;
        }
    }

}
