package com.example.testapp.models;

public class OrderItem {
    private String id;
    private double quantity;
    private String productId;
    private Product product;
    //private Order order;
    private double price;
    private double discount;
    private double amount;

    public OrderItem(Order order, Product product, double quantity, double discount) {
        this.quantity = quantity;
        this.product = product;
        productId = product.getId();
        //this.order = order;
        price = quantity * product.getPrice();
        this.discount = discount;
        this.amount = price - discount;
    }

    public OrderItem(Order order, String productId, double quantity) {
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
