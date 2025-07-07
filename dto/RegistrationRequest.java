package com.BankingApp.dto;

public class RegistrationRequest {
    private float initialBalance;
    private String password;

    public RegistrationRequest() {
    } // Required for JSON deserialization

    public RegistrationRequest(String password, int initialBalance) {
        this.password = password;
        this.initialBalance = initialBalance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(float initialBalance) {
        this.initialBalance = initialBalance;
    }
}
