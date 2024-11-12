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
public class loanFetchResponse {
    private Integer id;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private loanStudentResponse estudiante;
    private loanBookResponse libro;
    private String estado;
}
