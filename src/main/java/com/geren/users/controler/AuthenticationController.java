package com.geren.users.controler;

import com.geren.users.dto.*;
import com.geren.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;


    @PostMapping("/creatUser")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDTO user){
         userService.createUser(user);
         return ResponseEntity.status(HttpStatus.CREATED).body("Usuario criado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDTO user){
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(user));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO dto) {

        userService.generateResetToken(dto.email());

        return ResponseEntity.ok("Se o e-mail existir, você receberá instruções para redefinir a senha."
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDTO dto){
        userService.resetPassword(dto.token(), dto.newPassword());
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<Object> deleteUser(){
        userService.deleteUser();
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso.");
    }

    @PutMapping("update")
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserUpdateDTO dto){
        userService.updateUser(dto);
        return ResponseEntity.ok("Usuario atualizado com sucesso.");
    }

}
