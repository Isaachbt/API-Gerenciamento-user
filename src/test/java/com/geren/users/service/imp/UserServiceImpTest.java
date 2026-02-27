package com.geren.users.service.imp;

import com.geren.users.dto.UserDTO;
import com.geren.users.enums.RoleEnum;
import com.geren.users.exception.EmailAlreadyExistsException;
import com.geren.users.exception.ErroCadastro;
import com.geren.users.model.User;
import com.geren.users.repository.UserRepository;
import com.geren.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @InjectMocks
    private UserServiceImp userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserDTO  userDTO;


    @BeforeEach
    void setUp() {

        userDTO = new UserDTO("Isaac",
                "58948998578",
                LocalDate.of(2010, 5,20),
                "dto@gmail.com",
                "12345678");
    }

    @Test
    void createUserSuccess() {
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("hashedPassword");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        userService.createUser(userDTO);

        Mockito.verify(userRepository)
                .save(captor.capture());

        User userSaved = captor.getValue();

        assertEquals(userDTO.email(),userSaved.getEmail());
        assertEquals("hashedPassword",userSaved.getPassword());
        assertEquals(RoleEnum.USER,userSaved.getRole());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        Mockito.when(userRepository.existsByEmail(userDTO.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userDTO));

        Mockito.verify(userRepository,Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldThrowErroCadastroWhenSaveFails(){
        Mockito.when(userRepository.existsByEmail(userDTO.email())).thenReturn(false);

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("hashedPassword");
        Mockito.doThrow(new RuntimeException("Erro banco"))
                .when(userRepository)
                .save(Mockito.any(User.class));

        ErroCadastro exception = assertThrows(
                ErroCadastro.class,() -> userService.createUser(userDTO)
        );

        assertEquals("Não foi possivl cadastrar usuario.",exception.getMessage());

    }

    @Test
    void login() {
    }

    @Test
    void generateResetToken() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void profile() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}