package com.example.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Payment implements Parcelable {
    private String id, paymentMethod, paymentDate, userId, orderId, transactionId, status;
    private double amount, tendered, change;

    public Payment(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.status = "Pending";
    }

    protected Payment(Parcel in) {
        id = in.readString();
        paymentMethod = in.readString();
        paymentDate = in.readString();
        userId = in.readString();
        orderId = in.readString();
        transactionId = in.readString();
        amount = in.readDouble();
        tendered = in.readDouble();
        change = in.readDouble();
        status = in.readString();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public double getTendered() {
        return tendered;
    }

    public double getChange() {
        return change;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(paymentMethod);
        parcel.writeString(paymentDate);
        parcel.writeString(userId);
        parcel.writeString(orderId);
        parcel.writeString(transactionId);
        parcel.writeDouble(amount);
        parcel.writeDouble(tendered);
        parcel.writeDouble(change);
        parcel.writeString(status);
    }
}
