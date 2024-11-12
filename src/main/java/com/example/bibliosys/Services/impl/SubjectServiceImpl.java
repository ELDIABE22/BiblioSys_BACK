package com.example.bibliosys.Services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.Subject;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.subject.SubjectResponse;
import com.example.bibliosys.Repository.SubjectRepository;
import com.example.bibliosys.Services.SubjectService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> fetchAllSubjectsService() {
        Iterable<Subject> subjectsIterable = subjectRepository.findAll();
        List<Subject> subjects = new ArrayList<>();
        subjectsIterable.forEach(subjects::add);
        return subjects;
    }

    @Override
    public SubjectResponse newSubjectService(Subject subject) {
        Subject existingSubject = subjectRepository.findByNombre(subject.getNombre());

        if (existingSubject != null) {
            return SubjectResponse.builder()
                    .subject(null)
                    .message("El nombre de la materia ya está registrado.")
                    .build();
        }

        Subject subjectEntity = Subject.builder()
                .nombre(subject.getNombre())
                .build();

        Subject subjectSave = subjectRepository.save(subjectEntity);

        return SubjectResponse.builder()
                .subject(subjectSave)
                .message("Materia creada.")
                .build();
    }

    @Override
    public ApiResponse<Subject> updateSubjectService(Subject subject) {
        StoredProcedureQuery updateSubjectQuery = entityManager.createStoredProcedureQuery("sp_ActualizarMateria")
                .registerStoredProcedureParameter("IdMateria", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("Nombre", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

        updateSubjectQuery.setParameter("IdMateria", subject.getId());
        updateSubjectQuery.setParameter("Nombre", subject.getNombre());

        updateSubjectQuery.execute();

        String mensajeSalida = (String) updateSubjectQuery.getOutputParameterValue("MensajeSalida");

        if ("Materia no existe.".equals(mensajeSalida)
                || "El nombre de la materia ya está registrado.".equals(mensajeSalida)) {
            return ApiResponse.<Subject>builder()
                    .data(null)
                    .message(mensajeSalida)
                    .build();
        }

        Subject updatedSubject = subjectRepository.findById(subject.getId()).orElse(null);

        return ApiResponse.<Subject>builder()
                .data(updatedSubject)
                .message(mensajeSalida)
                .build();
    }

    @Override
    public ApiResponse<String> deleteSubjectService(Integer subjectId) {
        StoredProcedureQuery deleteSubjectQuery = entityManager.createStoredProcedureQuery("sp_EliminarMateria")
                .registerStoredProcedureParameter("IdMateria", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

        deleteSubjectQuery.setParameter("IdMateria", subjectId);
        deleteSubjectQuery.execute();

        String mensajeSalida = (String) deleteSubjectQuery.getOutputParameterValue("MensajeSalida");

        if ("Materia no encontrada.".equals(mensajeSalida)
                | "No se puede eliminar la materia. Primero elimine los libros asociados.".equals(mensajeSalida)) {
            return ApiResponse.<String>builder()
                    .data(null)
                    .message(mensajeSalida)
                    .build();
        }

        return ApiResponse.<String>builder()
                .data(null)
                .message(mensajeSalida)
                .build();
    }
}
