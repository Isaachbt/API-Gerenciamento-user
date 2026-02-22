package com.geren.users.exception;

public class UserNotCadastrado extends RuntimeException {
    public UserNotCadastrado(String message) {
        super(message);
    }
}
