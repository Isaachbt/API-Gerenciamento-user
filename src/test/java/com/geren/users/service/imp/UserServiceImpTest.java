package com.geren.users.service.imp;

import com.geren.users.dto.LoginDTO;
import com.geren.users.dto.UserDTO;
import com.geren.users.dto.UserUpdateDTO;
import com.geren.users.enums.RoleEnum;
import com.geren.users.exception.EmailAlreadyExistsException;
import com.geren.users.exception.ErroCadastro;
import com.geren.users.exception.ErrorTokenException;
import com.geren.users.exception.NotFound;
import com.geren.users.model.User;
import com.geren.users.repository.UserRepository;
import com.geren.users.service.AuthenticationService;
import com.geren.users.service.UserService;
import com.geren.users.utils.AuthenticationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private FakeEmailService fakeEmailService;
    private UserDTO  userDTO;
    private LoginDTO loginDTO;
    private UserUpdateDTO  userUpdateDTO;


    @BeforeEach
    void setUp() {

        userDTO = new UserDTO("Isaac",
                "58948998578",
                LocalDate.of(2010, 5,20),
                "dto@gmail.com",
                "12345678");

        loginDTO = new LoginDTO(userDTO.email(),  userDTO.password());
        userUpdateDTO = new UserUpdateDTO(userDTO.name(), userDTO.cpf(), userDTO.dataNascimento(), userDTO.email());


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

        assertEquals("Não foi possivel cadastrar usuario.",exception.getMessage());

    }

    @Test
    void loginSuccess() {

        Mockito.when(userRepository.findByEmail(loginDTO.email()))
                .thenReturn(Optional.of(new User()));

        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(authentication);

        String fakeToken = "token-123";

        Mockito.when(authenticationService.login(loginDTO))
                .thenReturn(fakeToken);

        String result = userService.login(loginDTO);

        assertEquals(fakeToken, result);
    }

    @Test
    void shouldThrowBadCredentialsExceptionWhenLoginFails() {
        Mockito.doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(Mockito.any());

        assertThrows(BadCredentialsException.class,() -> userService.login(loginDTO));
    }

    @Test
    void deleteUserSucesso() {
        Mockito.when(authenticationFacade.getCurrentUser()).thenReturn(new User());

        Mockito.when(userRepository.existsById(Mockito.any())).thenReturn(true);

        userService.deleteUser();
        Mockito.verify(userRepository)
                .deleteById(Mockito.any());
    }

    @Test
    void shouldThrowExceptionWhenExistByIdDeleteFails(){
        Mockito.when(authenticationFacade.getCurrentUser()).thenReturn(new User());
        Mockito.when(userRepository.existsById(Mockito.any())).thenReturn(false);

        assertThrows(NotFound.class,() -> userService.deleteUser());
        Mockito.verify(userRepository,Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    void getProfileSucess() {
        Mockito.when(authenticationFacade.getCurrentUser()).thenReturn(new User());
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new User()));

        var profile = userService.profile();

        assertNotNull(profile);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){
        Mockito.when(authenticationFacade.getCurrentUser()).thenReturn(new User());
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> userService.profile());
    }

    @Test
    void updateUserSucesso() {
        Mockito.when(authenticationFacade.getCurrentUser()).thenReturn(new User());
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new User()));

        userService.updateUser(userUpdateDTO);
        Mockito.verify(userRepository).save(Mockito.any(User.class));


    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundUpdate(){
        Mockito.when(authenticationFacade.getCurrentUser()).thenReturn(new User());
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(NotFound.class,() -> userService.updateUser(userUpdateDTO));

    }

    @Test
    void shouldGenerateResetTokenWhenUserExists() {
        var user = new User();
        user.setEmail(userDTO.email());
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        userService.generateResetToken(user.getEmail());
        Mockito.verify(userRepository).save(user);

        Mockito.verify(fakeEmailService).sendResetEmail(Mockito.eq(user.getEmail()),Mockito.anyString());

        assertTrue(user.getResetTokenExpiration().isAfter(LocalDateTime.now()));
        assertNotNull(user.getResetToken());
        assertNotNull(user.getResetTokenExpiration());



    }

    @Test
    void shouldDoNothingWhenUserDoesNotExist() {

        Mockito.when(userRepository.findByEmail("dto@gmail.com"))
                .thenReturn(Optional.empty());

        userService.generateResetToken("dto@gmail.com");

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any());

        Mockito.verify(fakeEmailService, Mockito.never())
                .sendResetEmail(Mockito.anyString(), Mockito.anyString());
    }

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