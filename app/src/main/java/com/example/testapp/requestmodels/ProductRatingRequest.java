package com.example.testapp.requestmodels;

public class ProductRatingRequest {
    private int rating;
    private String review;
    private String userId;

    public ProductRatingRequest( int rating, String review, String userId) {
        this.rating = rating;
        this.review = review;
        this.userId = userId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
