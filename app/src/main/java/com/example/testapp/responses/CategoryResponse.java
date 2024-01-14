package com.example.testapp.responses;

import com.example.testapp.models.Category;

import java.util.List;

public class CategoryResponse {
    private List<Category> data;

    public List<Category> getCategories() {
        return data;
    }

    public void setCategories(List<Category> category) {
        this.data = category;
    }
}
