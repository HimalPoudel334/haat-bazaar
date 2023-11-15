package com.example.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable {
    private String productName;
    private String productImage;
    private String productDescription;
    private double productPrice;
    private double productPreviousPrice;
    private String productUnit;
    private double productUnitChangeQuantity;
    private int productStock;
    private Category category;
    private SubCategory subCategory;

    public Product(String productName, String productImage, String productDescription, double productPrice, double productPreviousPrice, String productUnit, double productUnitChangeQuantity, int productStock) {
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productPreviousPrice = productPreviousPrice;
        this.productUnit = productUnit;
        this.productUnitChangeQuantity = productUnitChangeQuantity;
        this.productStock = productStock;
    }

    protected Product(Parcel in) {
        productName = in.readString();
        productImage = in.readString();
        productDescription = in.readString();
        productPrice = in.readDouble();
        productPreviousPrice = in.readDouble();
        productUnit = in.readString();
        productUnitChangeQuantity = in.readDouble();
        productStock = in.readInt();
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

    public double getProductUnitChangeQuantity() {
        return productUnitChangeQuantity;
    }

    public void setProductUnitChangeQuantity(double productUnitChangeQuantity) {
        this.productUnitChangeQuantity = productUnitChangeQuantity;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(productImage);
        parcel.writeString(productDescription);
        parcel.writeDouble(productPrice);
        parcel.writeDouble(productPreviousPrice);
        parcel.writeString(productUnit);
        parcel.writeDouble(productUnitChangeQuantity);
        parcel.writeInt(productStock);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}
