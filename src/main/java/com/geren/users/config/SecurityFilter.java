package com.geren.users.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.geren.users.model.User;
import com.geren.users.repository.UserRepository;
import com.geren.users.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extrairTokenHeader(request);

        if (token != null) {
            try {
                String login = authenticationService.validToken(token);

                repository.findByEmail(login).ifPresent(user -> {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });

            } catch (TokenExpiredException e) {
                handleTokenExpiration(token);
            }
        }
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);
    }

    private String extrairTokenHeader(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return null;
        }

        if (!authHeader.split(" ")[0].equals("Bearer")) {
            return null;
        }
        return authHeader.split(" ")[1];
    }

    private void handleTokenExpiration(String token) {
        String login = authenticationService.getLoginFromExpiredToken(token);
        Optional<User> user = repository.findByEmail(login);
        user.ifPresent(u -> {
            u.setOnline(false);
            repository.save(u);
        });
    }
}
