package com.example.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Category implements Parcelable {
    private String id;
    private String name;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

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

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }

    public static class CategoryCreate {
        private String name;

        public CategoryCreate(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
