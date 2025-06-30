package com.example.testapp.responses;

import com.example.testapp.models.Order;
import com.example.testapp.network.RetrofitClient;

import java.util.List;

public class OrderResponses {
    public static class SingleOrderResponse {
        private Order order;

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }
    }

    public static class MultiOrderResponses {
        private List<Order> orders;

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }
    }

    public static class NewOrderCountResponse {

        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public static class AllOrderResponse {
        public List<AllOrder> getOrders() {
            return orders;
        }

        public void setOrders(List<AllOrder> orders) {
            this.orders = orders;
        }

        private List<AllOrder> orders;
    }


    public static class AllOrder {
        private String id;
        private String createdOn;
        private String fulfilledOn;
        private String deliveryLocation;
        private double deliveryCharge;
        private String deliveryStatus;
        private String name;
        private String image;
        private double quantity;
        private String unit;
        private String status;
        private double totalPrice;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getFulfilledOn() {
            return fulfilledOn;
        }

        public void setFulfilledOn(String fulfilledOn) {
            this.fulfilledOn = fulfilledOn;
        }

        public String getDeliveryLocation() {
            return deliveryLocation;
        }

        public void setDeliveryLocation(String deliveryLocation) {
            this.deliveryLocation = deliveryLocation;
        }

        public double getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(double deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }

        public String getProductName() {
            return name;
        }

        public void setProductName(String productName) {
            this.name = productName;
        }

        public String getProductImage() {
            String processedImage = image;

            if (processedImage != null && processedImage.startsWith("/")) {
                processedImage = processedImage.substring(1);
            }

            return RetrofitClient.BASE_URL + processedImage;
        }
        public void setProductImage(String productImage) {
            this.image = productImage;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }
    }


}
