package com.example.testapp.models;

public class OrderDetail {
    private String id;
    private double quantity;
    private String productId;
    private Product product;
    //private Order order;
    private double price;

    public OrderDetail(Order order, Product product, double quantity) {
        this.quantity = quantity;
        this.product = product;
        productId = product.getId();
        //this.order = order;
        price = quantity * product.getPrice();
    }

    public OrderDetail(Order order, String productId, double quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
