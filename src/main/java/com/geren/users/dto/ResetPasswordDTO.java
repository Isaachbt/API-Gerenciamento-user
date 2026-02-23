package com.geren.users.dto;

import jakarta.validation.constraints.NotNull;

public record ResetPasswordDTO(@NotNull(message = "token necessario") String token,
                               @NotNull(message = "Informe a nova senha.") String newPassword) {
}
