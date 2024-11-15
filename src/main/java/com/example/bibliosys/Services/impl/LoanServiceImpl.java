package com.example.bibliosys.Services.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.Book;
import com.example.bibliosys.Models.Student;
import com.example.bibliosys.Models.request.loan.LoanRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.loan.LoanResponse;
import com.example.bibliosys.Models.response.loan.loanBookResponse;
import com.example.bibliosys.Models.response.loan.loanFetchResponse;
import com.example.bibliosys.Models.response.loan.loanOverdueResponse;
import com.example.bibliosys.Models.response.loan.loanStudentResponse;
import com.example.bibliosys.Repository.BookRepository;
import com.example.bibliosys.Repository.StudentRepository;
import com.example.bibliosys.Services.EmailService;
import com.example.bibliosys.Services.LoanService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class LoanServiceImpl implements LoanService {
        @Autowired
        private EntityManager entityManager;

        @Autowired
        private EmailService emailService;

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private BookRepository bookRepository;

        @Override
        public List<loanFetchResponse> fetchAllLoansService() {
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_ObtenerPrestamos");

                @SuppressWarnings("unchecked")
                List<Object[]> loans = query.getResultList();

                Map<Integer, loanFetchResponse> loanMap = new HashMap<>();

                for (Object[] row : loans) {
                        Integer loanId = (Integer) row[0];
                        loanMap.computeIfAbsent(loanId, id -> {
                                return loanFetchResponse.builder()
                                                .id(loanId)
                                                .fechaPrestamo((Date) row[1])
                                                .fechaDevolucion((Date) row[2])
                                                .estudiante(
                                                                loanStudentResponse.builder()
                                                                                .id((Integer) row[4])
                                                                                .nombres((String) row[5])
                                                                                .apellidos((String) row[6])
                                                                                .build())
                                                .libro(
                                                                loanBookResponse.builder()
                                                                                .id((Integer) row[7])
                                                                                .title((String) row[8])
                                                                                .build())
                                                .estado((String) row[3])
                                                .build();
                        });
                }

                return new ArrayList<>(loanMap.values());
        }

        @Override
        public List<loanOverdueResponse> fetchLoansOverdueService() {
                // Ejecutar el procedimiento de actualización
                StoredProcedureQuery updateQuery = entityManager
                                .createStoredProcedureQuery("sp_ActualizarPrestamosVencidos");
                updateQuery.execute();

                // Obtener los préstamos vencidos
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_ObtenerPrestamosVencidos");

                @SuppressWarnings("unchecked")
                List<Object[]> loansOverdue = query.getResultList();

                Map<Integer, loanOverdueResponse> loanMap = new HashMap<>();

                for (Object[] row : loansOverdue) {
                        Integer bookId = (Integer) row[1];
                        loanMap.computeIfAbsent(bookId, id -> {
                                return loanOverdueResponse.builder()
                                                .idEstudiante((Integer) row[0])
                                                .idLibro(bookId)
                                                .nombresEstudiante((String) row[2])
                                                .apellidosEstudiante((String) row[3])
                                                .correoEstudiante((String) row[4])
                                                .fechaPrestamo((Date) row[5])
                                                .fechaDevolucion((Date) row[6])
                                                .build();
                        });
                }

                return new ArrayList<>(loanMap.values());
        }

        @Override
        public ApiResponse<LoanResponse> newLoanService(LoanRequest loanRequest) {
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_InsertarPrestamo")
                                .registerStoredProcedureParameter("IdLibro", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("IdEstudiante", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("FechaPrestamo", java.sql.Date.class,
                                                ParameterMode.IN)
                                .registerStoredProcedureParameter("FechaDevolucion", java.sql.Date.class,
                                                ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                query.setParameter("IdLibro", loanRequest.getIdLibro());
                query.setParameter("IdEstudiante", loanRequest.getIdEstudiante());
                query.setParameter("FechaPrestamo", loanRequest.getFechaPrestamo());
                query.setParameter("FechaDevolucion", loanRequest.getFechaDevolucion());

                query.execute();
                String mensajeSalida = (String) query.getOutputParameterValue("MensajeSalida");

                if ("El libro no existe.".equals(mensajeSalida)
                                || "El estudiante no existe.".equals(mensajeSalida)
                                || "El libro ya está prestado y no ha sido devuelto.".equals(mensajeSalida)
                                || "El usuario ya tiene un libro prestado.".equals(mensajeSalida)) {
                        return ApiResponse.<LoanResponse>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                LoanResponse loanResponse = LoanResponse.builder()
                                .idEstudiante(loanRequest.getIdEstudiante())
                                .idLibro(loanRequest.getIdLibro())
                                .fechaPrestamo(loanRequest.getFechaPrestamo())
                                .fechaDevolucion(loanRequest.getFechaDevolucion())
                                .estado(loanRequest.getEstado())
                                .build();

                Optional<Book> bookOpt = bookRepository.findById(loanRequest.getIdLibro());
                Optional<Student> studentOpt = studentRepository.findById(loanRequest.getIdEstudiante());
                if (studentOpt.isPresent() | bookOpt.isPresent()) {
                        String emailContent = emailService.buildEmailLoanContent(
                                        studentOpt.get().getNombres(),
                                        bookOpt.get().getTitulo(),
                                        loanRequest.getFechaDevolucion().toString());

                        emailService.sendSimpleMessage(studentOpt.get().getCorreo(), "Préstamo de Libro", emailContent);
                }

                return ApiResponse.<LoanResponse>builder()
                                .data(loanResponse)
                                .message(mensajeSalida)
                                .build();
        }

        @Override
        public ApiResponse<Void> deleteLoanService(Integer loanId) {
                StoredProcedureQuery deleteLoanQuery = entityManager.createStoredProcedureQuery("sp_EliminarPrestamo")
                                .registerStoredProcedureParameter("PrestamoId", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                deleteLoanQuery.setParameter("PrestamoId", loanId);

                deleteLoanQuery.execute();
                String mensajeSalida = (String) deleteLoanQuery.getOutputParameterValue("MensajeSalida");

                if ("No se encontró el préstamo.".equals(mensajeSalida)) {
                        return ApiResponse.<Void>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                return ApiResponse.<Void>builder()
                                .data(null)
                                .message(mensajeSalida)
                                .build();
        }

        @Override
        public ApiResponse<LoanResponse> updateLoanService(LoanRequest loanRequest) {
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_ActualizarPrestamo")
                                .registerStoredProcedureParameter("IdPrestamo", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("IdLibro", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("IdEstudiante", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("FechaPrestamo", java.sql.Date.class,
                                                ParameterMode.IN)
                                .registerStoredProcedureParameter("FechaDevolucion", java.sql.Date.class,
                                                ParameterMode.IN)
                                .registerStoredProcedureParameter("Estado", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                query.setParameter("IdPrestamo", loanRequest.getId());
                query.setParameter("IdLibro", loanRequest.getIdLibro());
                query.setParameter("IdEstudiante", loanRequest.getIdEstudiante());
                query.setParameter("FechaPrestamo", loanRequest.getFechaPrestamo());
                query.setParameter("FechaDevolucion", loanRequest.getFechaDevolucion());
                query.setParameter("Estado", loanRequest.getEstado());

                query.execute();
                String mensajeSalida = (String) query.getOutputParameterValue("MensajeSalida");

                if ("El préstamo no existe.".equals(mensajeSalida)
                                | "El libro no existe.".equals(mensajeSalida)
                                | "El estudiante no existe.".equals(mensajeSalida)) {
                        return ApiResponse.<LoanResponse>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                LoanResponse loanResponse = LoanResponse.builder()
                                .id(loanRequest.getId())
                                .idEstudiante(loanRequest.getIdEstudiante())
                                .idLibro(loanRequest.getIdLibro())
                                .fechaPrestamo(loanRequest.getFechaPrestamo())
                                .fechaDevolucion(loanRequest.getFechaDevolucion())
                                .estado(loanRequest.getEstado())
                                .build();

                return ApiResponse.<LoanResponse>builder()
                                .data(loanResponse)
                                .message(mensajeSalida)
                                .build();
        }

        @Override
        public ApiResponse<Void> sendMailLoanOverdueService(String correo, String message) {
                String emailContent = emailService.buildOverdueLoanEmailContent(message);

                emailService.sendSimpleMessage(correo, "Devolución de Libro", emailContent);

                return ApiResponse.<Void>builder()
                                .data(null)
                                .message("Correo electrónico enviado.")
                                .build();
        }

}
