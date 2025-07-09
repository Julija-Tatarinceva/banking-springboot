package com.BankingApp.exception;

public class InvalidTransactionAmountException extends BusinessRuleViolationException  {
    public InvalidTransactionAmountException(String msg) {
        super(msg);
    }
}
