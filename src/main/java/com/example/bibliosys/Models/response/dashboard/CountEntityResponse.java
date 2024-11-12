package com.example.bibliosys.Models.response.dashboard;

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
public class CountEntityResponse {
    private Integer totalPrestamos;
    private Integer totalLibros;
    private Integer totalEstudiantes;
    private Integer totalUsuarios;
}
