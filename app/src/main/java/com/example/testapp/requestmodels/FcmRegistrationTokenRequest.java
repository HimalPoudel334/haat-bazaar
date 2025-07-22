package com.example.testapp.requestmodels;

public class FcmRegistrationTokenRequest {
    private String userId;
    private String fcmToken;

    public FcmRegistrationTokenRequest(String userId, String fcmToken) {
        this.userId = userId;
        this.fcmToken = fcmToken;
    }
}
