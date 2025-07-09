package com.BankingApp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    final String type;
    final BigDecimal amount;
    final Integer fromAccount;
    final Integer toAccount;
    final LocalDateTime timestamp;

//    public Transaction() {
//        System.out.println("Transaction created.");
//    }

    public Transaction(String type, BigDecimal amount, Integer from, Integer to) {
        this.type = type;
        this.amount = amount;
        this.fromAccount = from;
        this.toAccount = to;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public Integer getFromAccount() {
        return fromAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getToAccount() {
        return toAccount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return timestamp + ", " + type + ", " + amount + ", from " +
                (fromAccount != null ? fromAccount : "*ATM*") + ", to " + (toAccount != null ? toAccount : "*ATM*");
    }
}
