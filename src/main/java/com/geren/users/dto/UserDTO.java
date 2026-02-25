package com.geren.users.dto;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserDTO(@NotBlank(message = "Nome necessario.") String name,
                      @NotBlank(message = "CPF necessario.")
                      @CPF String cpf,
                      @NotNull(message = "Data de nascimento necessario.")
                      @Past(message = "Data invalida") LocalDate dataNascimento,
                      @NotBlank(message = "E-mail necessario.")
                      @Email @Pattern(
                              regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                              message = "Email inválido")String email,
                      @NotBlank(message = "Senhha necessario.")
                      @Size(min = 8, message = "Senha deve ter no minimo 8 caracteres") String password) {
}
