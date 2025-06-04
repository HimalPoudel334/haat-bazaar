package com.example.testapp.models;

import java.util.List;

public class KhaltiPayment {
    PaymentInfo paymentInfo;

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public static class UserInfo {
        private String name;
        private String email;
        private String phone;

        // Getters and setters (or lombok annotations if using lombok)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class AmountBreakdown {
        private String label;
        private int amount;

        // Getters and setters (or lombok annotations if using lombok)
        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    public static class ProductDetail {
        private String identity;
        private String name;
        private int totalPrice;
        private int quantity;
        private int unitPrice;

        // Getters and setters (or lombok annotations if using lombok)
        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
        }
    }

    public static class PaymentInfo {
        private String returnUrl;
        private String websiteUrl;
        private int amount;
        private String purchaseOrderId;
        private String purchaseOrderName;
        private UserInfo UserInfo;
        private List<AmountBreakdown> amountBreakdown;
        private List<ProductDetail> productDetails;
        private String merchantUsername;
        private String merchantExtra;

        // Getters and setters (or lombok annotations if using lombok)
        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getPurchaseOrderId() {
            return purchaseOrderId;
        }

        public void setPurchaseOrderId(String purchaseOrderId) {
            this.purchaseOrderId = purchaseOrderId;
        }

        public String getPurchaseOrderName() {
            return purchaseOrderName;
        }

        public void setPurchaseOrderName(String purchaseOrderName) {
            this.purchaseOrderName = purchaseOrderName;
        }

        public UserInfo getUserInfo() {
            return UserInfo;
        }

        public void setUserInfo(UserInfo UserInfo) {
            this.UserInfo = UserInfo;
        }

        public List<AmountBreakdown> getAmountBreakdown() {
            return amountBreakdown;
        }

        public void setAmountBreakdown(List<AmountBreakdown> amountBreakdown) {
            this.amountBreakdown = amountBreakdown;
        }

        public List<ProductDetail> getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(List<ProductDetail> productDetails) {
            this.productDetails = productDetails;
        }

        public String getMerchantUsername() {
            return merchantUsername;
        }

        public void setMerchantUsername(String merchantUsername) {
            this.merchantUsername = merchantUsername;
        }

        public String getMerchantExtra() {
            return merchantExtra;
        }

        public void setMerchantExtra(String merchantExtra) {
            this.merchantExtra = merchantExtra;
        }
    }

    public static class KhaltiPaymentConfirmPayload {
        private String pidx;
        private String orderId;

        public KhaltiPaymentConfirmPayload(String pidx, String orderId) {
            this.pidx = pidx;
            this.orderId = orderId;
        }

        public String getPidx() {
            return pidx;
        }

        public void setPidx(String pidx) {
            this.pidx = pidx;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }

}


