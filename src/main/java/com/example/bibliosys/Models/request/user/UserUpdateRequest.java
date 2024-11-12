package com.example.bibliosys.Models.request.user;

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
public class UserUpdateRequest {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String correo;
    private String rol;
    private String estado;
}
