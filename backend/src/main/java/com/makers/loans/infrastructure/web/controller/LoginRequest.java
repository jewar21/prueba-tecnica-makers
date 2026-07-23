package com.makers.loans.infrastructure.web.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato válido")
        String email,
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
