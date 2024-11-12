package com.example.bibliosys.Models.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String correo;
    private String rol;
    private String estado;
}
