package com.example.testapp.models;

public class ProductImage {
    private String id;
    private String imageName;

    public ProductImage(String id, String imageName) {
        this.id = id;
        this.imageName = imageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
