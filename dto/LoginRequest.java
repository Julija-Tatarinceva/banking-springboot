package com.BankingApp.dto;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {} // Required for JSON deserialization

    public LoginRequest(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
