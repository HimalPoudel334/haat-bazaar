package com.example.testapp.responses;

import com.example.testapp.models.Product;

import java.util.List;

public class ProductResponse {

    private List<Product> data;
    public List<Product> getProducts() {
        return data;
    }

    public void setProduct(List<Product> data) {
        this.data = data;
    }

}
