package com.example.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SubCategory implements Parcelable {
    private String id;
    private Category category;
    private String name;

    public SubCategory(String id, Category category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }

    protected SubCategory(Parcel in) {
        id = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        name = in.readString();
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel in) {
            return new SubCategory(in);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(category, i);
        parcel.writeString(name);
    }
}
