package com.example.testapp.responses;

public class KhaltiResponses {
    public static class KhaltiPidxResponse {
        private String pidx;
        private String paymentUrl;
        private String expiresAt;
        private int expiresIn;

        public String getPidx() {
            return pidx;
        }

        public void setPidx(String pidx) {
            this.pidx = pidx;
        }

        public String getPaymentUrl() {
            return paymentUrl;
        }

        public void setPaymentUrl(String paymentUrl) {
            this.paymentUrl = paymentUrl;
        }

        public String getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
    }

}
