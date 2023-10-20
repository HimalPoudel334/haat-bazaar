package com.example.testapp.models;

public class Product {
    private String productName;
    private String productImage;
    private String productDescription;
    private double productPrice;
    private double productPreviousPrice;
    private String productUnit;

    public Product(String productName, String productImage, String productDescription, double productPrice, double productPreviousPrice, String productUnit) {
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productPreviousPrice = productPreviousPrice;
        this.productUnit = productUnit;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductPreviousPrice() {
        return productPreviousPrice;
    }

    public void setProductPreviousPrice(double productPreviousPrice) {
        this.productPreviousPrice = productPreviousPrice;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
