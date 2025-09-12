package com.example.testapp.responses;

import com.example.testapp.models.User;

public class AuthResponses {
    public class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class RefreshResponse {
        private String accessToken;
        private String refreshToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class OtpResponse {
        private String message;
        private int expiresInMinutes;

        public String getMessage() {
            return message;
        }

        public int getExpiresInMinutes() { return expiresInMinutes; }
    }

    public static class VerifyOtpResponse {
        private String message;
        private boolean isValid;

        public String getMessage() {
            return message;
        }

        public boolean isValid() {
            return isValid;
        }
    }
}
