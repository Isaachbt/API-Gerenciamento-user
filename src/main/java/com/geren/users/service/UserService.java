package com.geren.users.service;

import com.geren.users.model.User;
import com.geren.users.model.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(UserDTO user);
    List<User> getAllUsers();
    User getUserById(UUID id);
    User updateUser(UUID id, UserDTO userDTO);
    void deleteUser(UUID id);
}
