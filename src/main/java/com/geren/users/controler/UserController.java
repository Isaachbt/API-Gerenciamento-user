package com.geren.users.controler;

import com.geren.users.dto.UserResponseDTO;
import com.geren.users.model.User;
import com.geren.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Perfil do usuario")
    public ResponseEntity<UserResponseDTO> profile(){
            return ResponseEntity.status(HttpStatus.OK).body(userService.profile());
    }
}
