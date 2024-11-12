package com.example.bibliosys.Models.request;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank String usuario,
        @NotBlank String contraseña) {
}
