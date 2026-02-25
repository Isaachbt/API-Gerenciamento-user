package com.geren.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(@NotBlank @Email String email,
                       @NotBlank @Size(min = 8,message = "Senha deve ter 8 caracteres no minimo.") String password) {
}
