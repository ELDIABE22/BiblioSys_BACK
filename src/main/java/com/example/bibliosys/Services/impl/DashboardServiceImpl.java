package com.example.bibliosys.Services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.response.dashboard.CountEntityResponse;
import com.example.bibliosys.Services.DashboardService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private EntityManager entityManager;

    @Override
    public CountEntityResponse fetchCountEntityService() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_DetallesPanel");

        @SuppressWarnings("unchecked")
        List<Object[]> details = query.getResultList();

        Object[] resultArray = details.get(0);

        CountEntityResponse countEntityResponse = CountEntityResponse.builder()
                .totalPrestamos(((Number) resultArray[0]).intValue() | 0)
                .totalLibros(((Number) resultArray[1]).intValue() | 0)
                .totalEstudiantes(((Number) resultArray[2]).intValue() | 0)
                .totalUsuarios(((Number) resultArray[3]).intValue() | 0)
                .build();

        return countEntityResponse;
    }
}
