package com.example.bibliosys.Models.request.loan;

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
public class LoanRequest {
    private Integer id;
    private Integer idEstudiante;
    private Integer idLibro;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private String estado;
}
