package com.BankingApp.dto;

public class LoginRequest {
    private int accountNumber;
    private String password;

    public LoginRequest() {} // Required for JSON deserialization

    public LoginRequest(String password, int accountNumber) {
        this.password = password;
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
