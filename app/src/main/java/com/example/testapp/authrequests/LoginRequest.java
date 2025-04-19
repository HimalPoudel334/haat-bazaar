package com.example.testapp.authrequests;

public class LoginRequest {

    private final String username;
    private final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
