package com.BankingApp.model;

import java.io.Serializable;
import java.util.Random;


public class BankAccount implements Serializable {
    private final int accountNumber;
    private String password;
    float balance;

    // Default constructor
    public BankAccount() {
        // We create a unique account number
        long timestamp = System.currentTimeMillis() % 10000000; // 7-digit base
        int random = new Random().nextInt(900) + 100; // 3-digit random
        this.accountNumber = Math.abs((int) ((timestamp * 1000) + random)); // might cause integer overflow in the future (2038+)
        balance = 0;
        System.out.println("Bank Account Constructor");
    }

    // Parameterized constructor
    public BankAccount(String password, float balance) {
        // Unique account number
        long timestamp = System.currentTimeMillis() % 10000000;
        int random = new Random().nextInt(900) + 100;
        this.accountNumber = Math.abs((int) ((timestamp * 1000) + random));
        this.password = password;
        this.balance = balance;
    }
    // Parameterized constructor
    public BankAccount(int id, String password, float balance) {
        this.accountNumber = id;
        this.password = password;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public float getBalance() {
        return balance;
    }

    // Login validation
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    // Method for increasing the balance
    public Transaction deposit(float amount) {
        this.balance += amount;
        return new Transaction("Deposit", amount, null, this.accountNumber);
    }

    // Method for decreasing the balance
    public Transaction withdraw(float amount) {
        this.balance -= amount;
        return new Transaction("Withdraw", amount, this.accountNumber, null);
    }

    // Displaying balance to user
    public void printBalance() {
        System.out.println("Your balance is: " + this.balance);
    }

    // Transfer balance to another account
    public Transaction transferTo(BankAccount account, float amount) {
        this.balance -= amount;
        account.deposit(amount);
        return new Transaction("Deposit", amount, this.accountNumber, account.getAccountNumber());
    }

}
