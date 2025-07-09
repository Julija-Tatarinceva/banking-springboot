package com.BankingApp.exception;

public class UserNotFoundException extends EntityNotFoundException  {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
