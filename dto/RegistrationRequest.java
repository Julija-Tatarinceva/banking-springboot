package com.BankingApp.dto;

public class RegistrationRequest {
    private String email;
    private String password;
    private String name;

    public RegistrationRequest() {
    } // Required for JSON deserialization

    public RegistrationRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
