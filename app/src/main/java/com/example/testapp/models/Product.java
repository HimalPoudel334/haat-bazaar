package com.example.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

public class Product implements Parcelable {
    private String id;
    private String name;
    private String image;
    private String description;
    private double price;
    private double previousPrice;
    private String unit;
    private double unitChange;
    private double stock;
    private String categoryId;
    private Category category;
    private SubCategory subCategory;

    public Product(String name, String image, String description, double price, double previousPrice, String unit, double unitChange, double stock, Category category) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.previousPrice = previousPrice;
        this.unit = unit;
        this.unitChange = unitChange;
        this.stock = stock;
        this.categoryId=category.getId();
        Log.d("ProductModel", "Product category id is: "+category.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(double previousPrice) {
        this.previousPrice = previousPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getUnitChange() {
        return unitChange;
    }

    public void setUnitChange(double unitChange) {
        this.unitChange = unitChange;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        description = in.readString();
        price = in.readDouble();
        previousPrice = in.readDouble();
        unit = in.readString();
        unitChange = in.readDouble();
        stock = in.readDouble();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(description);
        parcel.writeDouble(price);
        parcel.writeDouble(previousPrice);
        parcel.writeString(unit);
        parcel.writeDouble(unitChange);
        parcel.writeDouble(stock);
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
