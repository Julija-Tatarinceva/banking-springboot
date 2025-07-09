package com.BankingApp.exception;

public class TransferToSelfException extends BusinessRuleViolationException  {
    public TransferToSelfException(String msg) {
        super(msg);
    }
}
