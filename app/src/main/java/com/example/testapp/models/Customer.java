package com.example.testapp.models;

import com.example.testapp.basetypes.Location;
import com.example.testapp.basetypes.PhoneNumber;

public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private PhoneNumber mobileNumber;
    private Location location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public PhoneNumber getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(PhoneNumber mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLocation() {
        return location.toString();
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
