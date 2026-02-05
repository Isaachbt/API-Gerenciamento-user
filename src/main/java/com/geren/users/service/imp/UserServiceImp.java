package com.geren.users.service.imp;

import com.geren.users.exception.EmailAlreadyExistsException;
import com.geren.users.exception.NotFound;
import com.geren.users.model.User;
import com.geren.users.model.dto.UserDTO;
import com.geren.users.repository.UserRepository;
import com.geren.users.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(UserDTO user) {


        if (userRepository.existsByEmail(user.email())) {
            throw new EmailAlreadyExistsException("E-mail existente");
        }

        User usr = new User();
        BeanUtils.copyProperties(user, usr);
        return userRepository.save(usr);
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
