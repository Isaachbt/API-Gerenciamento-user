package com.geren.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(@NotBlank(message = "Nome necessario.") String name,
                      @NotBlank(message = "CPF necessario.") String cpf,
                      @NotBlank(message = "Data de nascimento necessario.") String dataNascimento,
                      @NotBlank(message = "E-mail necessario.") String email,
                      @NotBlank(message = "Senhha necessario.") String password) {
}
