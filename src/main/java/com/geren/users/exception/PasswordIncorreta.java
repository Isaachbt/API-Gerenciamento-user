package com.geren.users.exception;

public class PasswordIncorreta extends RuntimeException {
    public PasswordIncorreta(String message) {
        super(message);
    }
}
