package com.example.testapp.responses;

import com.example.testapp.models.User;

import java.util.List;

public class UserResponses {
    public class SingleUserResponse {
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public class MultiUserResponse {
        private List<User> users;

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }
}
