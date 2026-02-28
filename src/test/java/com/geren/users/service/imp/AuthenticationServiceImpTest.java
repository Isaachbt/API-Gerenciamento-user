package com.geren.users.service.imp;

import com.geren.users.exception.ErrorTokenException;
import com.geren.users.model.User;
import com.geren.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImpTest {

    @InjectMocks
    private UserServiceImp userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {

        Mockito.when(userRepository.findByResetToken("token123"))
                .thenReturn(Optional.empty());

        assertThrows(ErrorTokenException.class,
                () -> userService.resetPassword("token123", "novaSenha"));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any());
    }
    @Test
    void shouldThrowExceptionWhenTokenIsExpired() {

        User user = new User();
        user.setResetToken("token123");
        user.setResetTokenExpiration(LocalDateTime.now().minusMinutes(1));

        Mockito.when(userRepository.findByResetToken("token123"))
                .thenReturn(Optional.of(user));

        assertThrows(ErrorTokenException.class,
                () -> userService.resetPassword("token123", "novaSenha"));

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void shouldResetPasswordSuccessfully() {

        User user = new User();
        user.setResetToken("token123");
        user.setResetTokenExpiration(LocalDateTime.now().plusMinutes(10));

        Mockito.when(userRepository.findByResetToken("token123"))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.encode("novaSenha"))
                .thenReturn("senhaCriptografada");

        userService.resetPassword("token123", "novaSenha");

        assertEquals("senhaCriptografada", user.getPassword());
        assertNull(user.getResetToken());
        assertNull(user.getResetTokenExpiration());

        Mockito.verify(userRepository).save(user);
    }

}