package com.geren.users.service;

import com.geren.users.model.User;
import com.geren.users.dto.LoginDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {

    String login(LoginDTO dto);
    String obterToken(LoginDTO dto);
    String validToken(String token);
    String gerarTokenJwt(User user);
    String getLoginFromExpiredToken(String token);
}
