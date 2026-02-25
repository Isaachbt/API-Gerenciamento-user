package com.geren.users.dto;

import com.geren.users.enums.RoleEnum;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDTO(UUID id,
                              String name,
                              String cpf,
                              LocalDate dataNascimento,
                              String email,
                              RoleEnum role,
                              boolean online) {
}
