package com.geren.users.controler;

import com.geren.users.dto.UserResponseDTO;
import com.geren.users.model.User;
import com.geren.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getAllUser(){
            return ResponseEntity.status(HttpStatus.OK).body(userService.profile());
    }
}
