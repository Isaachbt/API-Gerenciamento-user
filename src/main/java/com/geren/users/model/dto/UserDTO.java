package com.geren.users.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(@NotBlank(message = "Nome necessario.") String name,
                      @NotBlank(message = "E-mail necessario.") String email,
                      @NotBlank(message = "Senhha necessario.") String password) {
}
