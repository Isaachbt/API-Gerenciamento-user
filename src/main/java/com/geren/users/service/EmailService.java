package com.geren.users.service;

public interface EmailService {
    void sendResetEmail(String to, String token);
}
