package com.example.testapp.requestmodels;

public class AuthRequest {
    public static class LoginRequest {

        private final String username;
        private final String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getEmail() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class PasswordResetRequest {
        private String email;
        public PasswordResetRequest(String email) {
            this.email = email;
        }
    }

    public static class VerifyOtpRequest {
        private String email;
        private String otpCode;
        public VerifyOtpRequest(String email, String otp) {
            this.email = email;
            this.otpCode = otp;
        }
    }

    public static class NewPasswordRequest {
        private String email;
        private String otpCode;
        private String newPassword;

        public NewPasswordRequest(String email, String otp, String newPassword) {
            this.email = email;
            this.otpCode = otp;
            this.newPassword = newPassword;
        }
    }
}

