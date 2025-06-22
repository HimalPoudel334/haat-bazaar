package com.example.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.testapp.basetypes.Location;

import java.util.Locale;

public class Shipment implements Parcelable {
    private String id, shipDate, address, city, state, country, zipCode, orderId, assignedTo, firstName, lastName, status;

    protected Shipment(Parcel in) {
        id = in.readString();
        shipDate = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        zipCode = in.readString();
        orderId = in.readString();
        assignedTo = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        status = in.readString();
    }

    public static final Creator<Shipment> CREATOR = new Creator<Shipment>() {
        @Override
        public Shipment createFromParcel(Parcel in) {
            return new Shipment(in);
        }

        @Override
        public Shipment[] newArray(int size) {
            return new Shipment[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedUserName() {
        return String.format(Locale.ENGLISH, "%s %s", firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getLocation() {
        return String.format("%s, %s, %s, %s, %s", address, zipCode, city, state, country);
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(shipDate);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(country);
        parcel.writeString(zipCode);
        parcel.writeString(orderId);
        parcel.writeString(assignedTo);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(status);
    }
}
