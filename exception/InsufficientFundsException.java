package com.BankingApp.exception;

public class InsufficientFundsException extends BusinessRuleViolationException  {
    public InsufficientFundsException(String msg) {
        super(msg);
    }
}
