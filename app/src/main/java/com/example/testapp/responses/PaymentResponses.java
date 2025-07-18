package com.example.testapp.responses;

import com.example.testapp.models.Payment;
import com.example.testapp.models.Shipment;

import java.util.List;

public class PaymentResponses {

    public class SinglePaymentResponse {
        private Payment payment;

        public Payment getPayment() {
            return payment;
        }

        public void setPayment(Payment payment) {
            this.payment = payment;
        }
    }

    public class MultiPaymentResponse {

        private List<Payment> payments;

        public List<Payment> getPayments() {
            return payments;
        }

        public void setPayments(List<Payment> payments) {
            this.payments = payments;
        }
    }
}
