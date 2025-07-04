package com.mana.library.exceptionhandler.exception;

public class AccountExpiredException extends RuntimeException {
    public AccountExpiredException(String message) {
        super(message);
    }
}
