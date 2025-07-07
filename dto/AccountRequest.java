package com.BankingApp.dto;

public class AccountRequest {
    private String password;
    private float balance;

    public AccountRequest() {} // Required for JSON deserialization

    public AccountRequest(String password, float balance) {
        this.password = password;
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
