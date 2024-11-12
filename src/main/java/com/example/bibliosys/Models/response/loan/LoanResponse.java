package com.example.bibliosys.Models.response.loan;

import java.sql.Date;

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
public class LoanResponse {
    private Integer id;
    private Integer idEstudiante;
    private Integer idLibro;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private String estado;
}
