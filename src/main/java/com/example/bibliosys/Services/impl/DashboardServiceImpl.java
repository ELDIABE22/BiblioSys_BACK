package com.example.bibliosys.Services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.response.dashboard.LoanAmountXDay;
import com.example.bibliosys.Models.response.dashboard.LoanAmountXMonth;
import com.example.bibliosys.Models.response.dashboard.LoansTopBook;
import com.example.bibliosys.Models.response.dashboard.DashboardResponse;
import com.example.bibliosys.Services.DashboardService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private EntityManager entityManager;

    private static final List<String> MONTHS = Arrays.asList("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago",
            "Sep", "Oct", "Nov", "Dic");

    public DashboardResponse fetchPanelDetails() {
        Query query = entityManager.createNativeQuery("SELECT * FROM vw_DetallesPanel");

        @SuppressWarnings("unchecked")
        List<Object[]> resultSet = query.getResultList();

        Object[] countResult = resultSet.get(0);

        return DashboardResponse.builder().totalPrestamos(((Number) countResult[0]).intValue())
                .totalLibros(((Number) countResult[1]).intValue())
                .totalEstudiantes(((Number) countResult[2]).intValue())
                .totalUsuarios(((Number) countResult[3]).intValue()).build();
    }

    private List<LoanAmountXDay> fetchLoanAmountXDay() {
        Query query = entityManager.createNativeQuery("SELECT * FROM vw_CantidadPrestamosPorDia");

        @SuppressWarnings("unchecked")
        List<Object[]> loanResults = query.getResultList();

        return loanResults.stream().map(result -> LoanAmountXDay.builder()
                .dia((String) result[0])
                .prestamos(((Number) result[1]).intValue())
                .build())
                .collect(Collectors.toList());
    }

    private List<LoanAmountXMonth> fetchLoanAmountXMonth() {
        Query query = entityManager.createNativeQuery("SELECT * FROM vw_CantidadPrestamosPorMes");

        @SuppressWarnings("unchecked")
        List<Object[]> loanResults = query.getResultList();

        Map<Integer, Integer> loanMap = loanResults.stream()
                .collect(Collectors.toMap(
                        result -> (Integer) result[0],
                        result -> ((Number) result[1]).intValue()));

        return IntStream.range(0, MONTHS.size())
                .mapToObj(index -> LoanAmountXMonth.builder()
                        .mes(MONTHS.get(index))
                        .total(loanMap.getOrDefault(index + 1, 0))
                        .build())
                .collect(Collectors.toList());
    }

    private List<LoansTopBook> fetchTop5Books() {
        Query query = entityManager.createNativeQuery("SELECT * FROM Top5LibrosMasPrestados");

        @SuppressWarnings("unchecked")
        List<Object[]> bookResults = query.getResultList();

        return bookResults.stream().map(
                result -> LoansTopBook.builder()
                        .libro((String) result[0])
                        .total(((Number) result[1]).intValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DashboardResponse fetchDashboardDataService() {
        DashboardResponse panelDetails = fetchPanelDetails();
        List<LoanAmountXDay> loanAmountXDay = fetchLoanAmountXDay();
        List<LoanAmountXMonth> loanAmountXMonth = fetchLoanAmountXMonth();
        List<LoansTopBook> top5Books = fetchTop5Books();

        return DashboardResponse.builder()
                .totalPrestamos(panelDetails.getTotalPrestamos())
                .totalLibros(panelDetails.getTotalLibros())
                .totalEstudiantes(panelDetails.getTotalEstudiantes())
                .totalUsuarios(panelDetails.getTotalUsuarios())
                .cantidadPrestamosPorDia(loanAmountXDay)
                .cantidadPrestamosPorMes(loanAmountXMonth)
                .top5LibrosMasPrestados(top5Books)
                .build();
    }
}
