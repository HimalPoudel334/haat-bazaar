package com.example.testapp.responses;

import com.example.testapp.models.Cart;

import java.util.List;

public class CartResponses {

    public class SingleCartResponse {
        private Cart cart;

        public Cart getCart() {
            return cart;
        }

        public void setCart(Cart cart) {
            this.cart = cart;
        }
    }

    public class MultiCartResponse {

        private List<Cart> carts;

        public List<Cart> getCarts() {
            return carts;
        }

        public void setCarts(List<Cart> carts) {
            this.carts = carts;
        }
    }
}
