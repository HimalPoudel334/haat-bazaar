package com.example.testapp.models;

public class SubCategory {
    private String id;
    private Category category;
    private String name;

    public SubCategory(String id, Category category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}
