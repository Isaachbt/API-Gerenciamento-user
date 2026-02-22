package com.geren.users.service.imp;

import com.geren.users.dto.LoginDTO;
import com.geren.users.enums.RoleEnum;
import com.geren.users.exception.EmailAlreadyExistsException;
import com.geren.users.exception.ErroCadastro;
import com.geren.users.exception.NotFound;
import com.geren.users.exception.PasswordIncorreta;
import com.geren.users.model.User;
import com.geren.users.dto.UserDTO;
import com.geren.users.repository.UserRepository;
import com.geren.users.service.AuthenticationService;
import com.geren.users.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    public String generateResetToken(UUID id) {
        return "";
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFound("Usuario não encontrado");
        }
        return user.get();
    }

    @Override
    public User updateUser(UUID id, UserDTO userDTO) {
        return null;
    }

    @Override
    public void deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            throw new NotFound("Usuario não encontrado");
        }
        userRepository.deleteById(id);
    }
}
