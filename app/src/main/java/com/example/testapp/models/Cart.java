package com.example.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.testapp.network.RetrofitClient;

public class Cart {
    private String id;
    private String productId;
    private String productName;
    private String image;
    private double quantity;
    private double rate;
    private String createdOn;
    private String sku;
    private double productStock;
    private double productUnitChange;
    private boolean isChecked;

    public Cart(String productId, double quantity, String createdOn) {
        this.productId = productId;
        this.quantity = quantity;
        this.createdOn = createdOn;
        isChecked = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        String processedImage = image;

        if (processedImage != null && processedImage.startsWith("/")) {
            processedImage = processedImage.substring(1);
        }

        return RetrofitClient.BASE_URL + processedImage;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRate() {
        return rate;
    }


    public double getPrice() {
        return this.quantity * this.rate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getProductStock() {
        return productStock;
    }

    public void setProductStock(double productStock) {
        this.productStock = productStock;
    }

    public double getProductUnitChange() {
        return productUnitChange;
    }

    public void setProductUnitChange(double productUnitChange) {
        this.productUnitChange = productUnitChange;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
