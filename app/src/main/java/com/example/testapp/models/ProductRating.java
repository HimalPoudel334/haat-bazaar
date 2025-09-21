package com.example.testapp.models;

import java.util.Locale;

public class ProductRating {
    private String id;
    private String firstName;
    private String lastName;
    private double rating;
    private String review;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return id;
    }

    public String getUserName() {
        return String.format(Locale.ENGLISH, "%s %s", firstName, lastName);
    }

    public String getInitials() {
        return String.format(Locale.ENGLISH, "%s%s", firstName.charAt(0), lastName.charAt(0));
    }

    public double getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
