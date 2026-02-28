package com.geren.users.service;

import com.geren.users.dto.LoginDTO;
import com.geren.users.dto.UserResponseDTO;
import com.geren.users.dto.UserUpdateDTO;
import com.geren.users.model.User;
import com.geren.users.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void createUser(UserDTO userDTO);
    String login(LoginDTO loginDTO);
    void generateResetToken(String email);
    public void resetPassword(String token, String newPassword);
    UserResponseDTO profile();
    void updateUser(UserUpdateDTO dto);
    void deleteUser();
}
