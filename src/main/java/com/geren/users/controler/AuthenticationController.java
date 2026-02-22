package com.geren.users.controler;

import com.geren.users.dto.LoginDTO;
import com.geren.users.dto.UserDTO;
import com.geren.users.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;


    @PostMapping("creatUser")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO user){
         userService.createUser(user);
         return ResponseEntity.status(HttpStatus.CREATED).body("Usuario criado com sucesso: ");
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDTO user){
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(user));
    }

    @PostMapping("forgot-password/{id}")
    public ResponseEntity<Object> forgotPassword(@PathVariable(value = "id")@NotNull UUID id){

        return ResponseEntity.status(HttpStatus.OK).body("Senha alterada com sucesso");
    }

}
