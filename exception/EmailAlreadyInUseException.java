package com.BankingApp.exception;

public class EmailAlreadyInUseException extends BusinessRuleViolationException  {
    public EmailAlreadyInUseException(String msg) {
        super(msg);
    }
}
