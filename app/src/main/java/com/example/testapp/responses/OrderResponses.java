package com.example.testapp.responses;

import com.example.testapp.models.Order;

import java.util.List;

public class OrderResponses {
    public class SingleOrderResponse {
        private Order order;

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }
    }

    public class MultiOrderResponses {
        private List<Order> orders;

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }
    }
}
