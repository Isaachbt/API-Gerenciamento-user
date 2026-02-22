package com.geren.users.service;

import com.geren.users.dto.LoginDTO;
import com.geren.users.model.User;
import com.geren.users.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void createUser(UserDTO userDTO);
    String login(LoginDTO loginDTO);
    String generateResetToken(UUID id);
    List<User> getAllUsers();
    User getUserById(UUID id);
    User updateUser(UUID id, UserDTO userDTO);
    void deleteUser(UUID id);
}
