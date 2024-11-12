package com.example.bibliosys.Models.request;

import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
                @NotBlank String nombres,
                @NotBlank String apellidos,
                @NotBlank String usuario,
                @NotBlank String correo,
                @NotBlank String contraseña) {
}
