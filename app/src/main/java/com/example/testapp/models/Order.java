package com.example.testapp.models;
import android.util.Log;

import com.example.testapp.basetypes.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Order {
    private String createdOn;
    private String fulfilledOn;
    private User User;
    private String UserId;
    private String deliveryLocation;
    private String deliveryStatus;
    private double totalPrice = 0.0;
    private double deliveryCharge;
    private List<OrderDetail> orderDetails;

    public Order(User User, String location, double deliveryCharge) {
        this.User = User;
        UserId = User.getId();
        deliveryStatus = "Pending";
        deliveryLocation = location;
        this.deliveryCharge = deliveryCharge;
        totalPrice += deliveryCharge;
        Date orderDate = GregorianCalendar.getInstance().getTime();
        createdOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(orderDate);
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
        return User;
    }

    public void setUser(User User) {
        this.User = User;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
        orderDetails.forEach(od -> {
            totalPrice += od.getPrice();
        });
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addOrderDetail(OrderDetail detail) {
        if(orderDetails == null) {
            orderDetails = new ArrayList<OrderDetail>();
            orderDetails.add(detail);
        }else {
            orderDetails.add(detail);
        }
        totalPrice += detail.getPrice();
    }
}
