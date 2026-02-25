package com.geren.users.service.imp;

import com.geren.users.dto.LoginDTO;
import com.geren.users.dto.UserResponseDTO;
import com.geren.users.enums.RoleEnum;
import com.geren.users.exception.*;
import com.geren.users.model.User;
import com.geren.users.dto.UserDTO;
import com.geren.users.repository.UserRepository;
import com.geren.users.service.AuthenticationService;
import com.geren.users.service.UserService;
import com.geren.users.utils.AuthenticationFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private FakeEmailService fakeEmailService;


    @Override
    public void createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new EmailAlreadyExistsException("E-mail existente");
        }

        var passWordHas = passwordEncoder.encode(userDTO.password());
        var authUser = new User();
        BeanUtils.copyProperties(userDTO, authUser);
        authUser.setPassword(passWordHas);
        authUser.setRole(RoleEnum.USER);

        try{
            userRepository.save(authUser);
        }catch (Exception e){
            throw new ErroCadastro("Não foi possivl cadastrar usuario.");
        }
    }

    @Override
    public String login(LoginDTO loginDTO) {
        try {

            var authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.email(),
                            loginDTO.password()
                    );

            authenticationManager.authenticate(authenticationToken);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }

        User user = userRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new UsernameNotFoundException("Email ou senha inválidos"));

        user.setOnline(true);
        userRepository.save(user);

        return authenticationService.login(loginDTO);
    }

    @Override
    public void generateResetToken(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {

            String token = UUID.randomUUID().toString();

            user.setResetToken(token);
            user.setResetTokenExpiration(
                    LocalDateTime.now().plusMinutes(15)
            );

            userRepository.save(user);

            fakeEmailService.sendResetEmail(email,token);
        });
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new ErrorTokenException("Token inválido"));

        if (user.getResetTokenExpiration()
                .isBefore(LocalDateTime.now())) {
            throw new ErrorTokenException("Token expirado");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiration(null);

        userRepository.save(user);
    }

    @Override
    public UserResponseDTO profile() {
        Optional<User> userOptional = userRepository.findById(authenticationFacade.getCurrentUser().getId());
        if (userOptional.isEmpty()) {
            throw new NotFound("Usuario não encontrado");
        }
        User user = userOptional.get();

        return new UserResponseDTO(user.getId(),user.getName(),user.getCpf(),user.getDataNascimento(),
                user.getEmail(),user.getRole(),user.isOnline());
    }

    @Override
    public User updateUser(UUID id, UserDTO userDTO) {
        return null;
    }

    @Override
    public void deleteUser() {
        var id = authenticationFacade.getCurrentUser().getId();
        if (!userRepository.existsById(id)) {
            throw new NotFound("Não foi possivel deletar usuario.");
        }
        userRepository.deleteById(id);
    }
}
