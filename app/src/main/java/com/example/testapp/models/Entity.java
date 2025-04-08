package com.example.testapp.models;

public class Entity {
    private String name;
    private int imageResourceId;
    private String imagePath;

    public Entity(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public Entity(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}