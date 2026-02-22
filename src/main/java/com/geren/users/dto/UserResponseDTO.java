package com.geren.users.dto;

import java.util.UUID;

public record UserResponseDTO(UUID id,String name,String cpf,String dataNascimento,String email,String password) {
}
