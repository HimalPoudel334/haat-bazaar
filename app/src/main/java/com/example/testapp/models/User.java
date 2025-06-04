package com.example.testapp.models;

import com.example.testapp.basetypes.Location;
import com.example.testapp.basetypes.PhoneNumber;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String username;
    private String userType;
    private String location;

    public User(String id, String firstName, String lastName, String phoneNumber, String email, String username, String location, String userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.location = location;
        this.userType = userType;
    }

    public User() {
        this.id = null;
        this.firstName = null;
        this.lastName = null;
        this.phoneNumber = null;
        this.email = null;
        this.username = null;
        this.location = null;
        this.userType = "GUEST";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber.getValue();
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isAdmin() {
        return userType != null && userType.equalsIgnoreCase("ADMIN");
    }

    public boolean isLoggedIn() {
        return id != null;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location.toString();
    }
}
