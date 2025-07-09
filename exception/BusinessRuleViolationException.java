package com.BankingApp.exception;

public abstract class BusinessRuleViolationException  extends RuntimeException {
    public BusinessRuleViolationException (String message) {
        super(message);
    }
}