package com.geren.users.controler;

import com.geren.users.model.User;
import com.geren.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("user/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("getAllUser")
    public ResponseEntity<List<User>> getAllUser(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("getOneUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") UUID id){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
