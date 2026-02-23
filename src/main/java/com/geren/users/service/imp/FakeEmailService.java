package com.geren.users.service.imp;

import com.geren.users.service.EmailService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public class FakeEmailService implements EmailService {

    @Override
    public void sendResetEmail(String to, String token) {
        System.out.println("=== RESET TOKEN ===");
        System.out.println("Email: " + to);
        System.out.println("Token: " + token);
        System.out.println("===================");
    }
}
