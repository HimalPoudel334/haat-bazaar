package com.example.testapp.models;
import com.example.testapp.basetypes.Location;

import java.util.Date;
import java.util.List;

public class Order {
    private Date orderCreationDate;
    private Date orderFulfilledDate;
    private Customer customer;
    private Location deliveryLocation;
    private String deliveryStatus;
    private List<OrderDetail> orderDetails;

}
