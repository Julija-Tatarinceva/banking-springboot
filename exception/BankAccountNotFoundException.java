package com.BankingApp.exception;

public class BankAccountNotFoundException extends EntityNotFoundException  {
    public BankAccountNotFoundException(String msg) {
        super(msg);
    }
}
