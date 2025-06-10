package com.example.testapp.models;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.testapp.basetypes.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Order {
    private String id;
    private String createdOn;
    private String fulfilledOn;
    private User user;
    private String userId;
    private String deliveryLocation;
    private String deliveryStatus;
    private double totalPrice = 0.0;
    private double totalQuantity = 0.0;
    private double deliveryCharge;
    private String paymentMethod = "Cash";
    private List<OrderItem> orderItems;

    public Order(User user, String location, double deliveryCharge) {
        this.user = user;
        userId = user.getId();
        deliveryStatus = "Pending";
        deliveryLocation = location;
        this.deliveryCharge = deliveryCharge;
        totalPrice += deliveryCharge;
        Date orderDate = GregorianCalendar.getInstance().getTime();
        createdOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(orderDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCreationDate() {
        return createdOn;
    }

    public void setOrderCreationDate(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getOrderFulfilledDate() {
        return fulfilledOn;
    }

    public void setOrderFulfilledDate(String fulfilledOn) {
        this.fulfilledOn = fulfilledOn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User User) {
        this.user = User;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String UserId) {
        this.userId = UserId;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        orderItems.forEach(od -> {
            totalPrice += od.getPrice();
            totalQuantity += od.getQuantity();
        });
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void addOrderItem(OrderItem detail) {
        if(orderItems == null) {
            orderItems = new ArrayList<OrderItem>();
            orderItems.add(detail);
        }else {
            orderItems.add(detail);
        }
        totalPrice += detail.getPrice();
    }

    public String getOrderStatusBgColor() {
        if (deliveryStatus == null) {
            return "#FFFFFF"; // default white for null
        }

        switch (deliveryStatus.trim().toLowerCase()) {
            case "payment pending":
                return "#FFA500"; // orange
            case "pending":
                return "#FFFF00"; // yellow
            case "processed":
                return "#87CEEB"; // sky blue
            case "out for delivery":
                return "#00BFFF"; // deep sky blue
            case "delivered":
                return "#32CD32"; // lime green
            case "canceled":
                return "#FF6347"; // tomato red
            default:
                return "#D3D3D3"; // light gray for unknown status
        }
    }
}
