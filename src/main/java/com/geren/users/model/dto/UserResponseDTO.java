package com.geren.users.model.dto;

import java.util.UUID;

public record UserResponseDTO(UUID id,String name,String email,String password) {
}
