package com.example.bibliosys.Models.response.dashboard;

import java.util.List;

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
public class DashboardResponse {
    private Integer totalPrestamos;
    private Integer totalLibros;
    private Integer totalEstudiantes;
    private Integer totalUsuarios;
    private List<LoanAmountXDay> cantidadPrestamosPorDia;
    private List<LoanAmountXMonth> cantidadPrestamosPorMes;
    private List<LoansTopBook> top5LibrosMasPrestados;
}
