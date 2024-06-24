package com.example.testapp.models;
import android.util.Log;

import com.example.testapp.basetypes.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Order {
    private String createdOn;
    private String fulfilledOn;
    private Customer customer;
    private String customerId;
    private String deliveryLocation;
    private String deliveryStatus;
    private double totalPrice = 0.0;
    private double deliveryCharge;
    private List<OrderDetail> orderDetails;

    public Order(Customer customer, String location, double deliveryCharge) {
        this.customer = customer;
        customerId = customer.getId();
        deliveryStatus = "Pending";
        deliveryLocation = location;
        this.deliveryCharge = deliveryCharge;
        totalPrice += deliveryCharge;
        Date orderDate = GregorianCalendar.getInstance().getTime();
        createdOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderDate);
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
