package com.example.testapp.responses;

import com.example.testapp.models.User;

public class AuthResponses {
    public class LoginResponse {
        private String token;
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }


        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
