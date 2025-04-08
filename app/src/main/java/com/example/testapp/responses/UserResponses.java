package com.example.testapp.responses;

import com.example.testapp.models.User;

import java.util.List;

public class UserResponses {
    public class SingleUserResponse {
        private User User;

        public User getUser() {
            return User;
        }

        public void setUser(User User) {
            this.User = User;
        }
    }

    public class MultiUserResponse {
        private List<User> Users;

        public List<User> getUsers() {
            return Users;
        }

        public void setUsers(List<User> Users) {
            this.Users = Users;
        }
    }
}
