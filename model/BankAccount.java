package com.BankingApp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Random;

@Entity
@Table(name = "bank_accounts")
public class BankAccount implements Serializable {
    @Id
    private final int id;
    BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Foreign key, handles authorization

    // Default constructor
    public BankAccount() {
        // We create a unique account number
        long timestamp = System.currentTimeMillis() % 10000000; // 7-digit base
        int random = new Random().nextInt(900) + 100; // 3-digit random
        this.id = Math.abs((int) ((timestamp * 1000) + random)); // might cause integer overflow in the future (2038+)
        balance = new BigDecimal("0");
        System.out.println("Bank Account Constructor");
    }

    // Parameterized constructor
    public BankAccount(BigDecimal balance) {
        // Unique account number
        long timestamp = System.currentTimeMillis() % 10000000;
        int random = new Random().nextInt(900) + 100;
        this.id = Math.abs((int) ((timestamp * 1000) + random));
        this.balance = balance;
    }
    // Parameterized constructor
    public BankAccount(User user, BigDecimal balance) {
        // Unique account number
        long timestamp = System.currentTimeMillis() % 10000000;
        int random = new Random().nextInt(900) + 100;
        this.id = Math.abs((int) ((timestamp * 1000) + random));
        this.user = user;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    // Method for increasing the balance
    public Transaction deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        return new Transaction("Deposit", amount, null, this.id);
    }

    // Method for decreasing the balance
    public Transaction withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        return new Transaction("Withdraw", amount, this.id, null);
    }

    // Transfer balance to another account
    public Transaction transferTo(BankAccount account, BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
        account.deposit(amount);
        return new Transaction("Deposit", amount, this.id, account.getId());
    }

}
