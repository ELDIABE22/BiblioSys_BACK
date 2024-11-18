package com.example.bibliosys.Services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.Student;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Repository.StudentRepository;
import com.example.bibliosys.Services.StudentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class StudentServiceImpl implements StudentService {
        @Autowired
        private EntityManager entityManager;

        @Autowired
        private StudentRepository studentRepository;

        @Override
        public List<Student> fetchAllStudentsService() {
                Iterable<Student> studentIterable = studentRepository.findAll();
                List<Student> students = new ArrayList<>();
                studentIterable.forEach(students::add);
                return students;
        }

        @Override
        public ApiResponse<Student> newStudentService(Student student) {
                StoredProcedureQuery query = entityManager
                                .createStoredProcedureQuery("sp_InsertarEstudiante")
                                .registerStoredProcedureParameter("Nombres", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Apellidos", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Correo", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Direccion", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Telefono", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Carrera", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Foto", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                query.setParameter("Nombres", student.getNombres());
                query.setParameter("Apellidos", student.getApellidos());
                query.setParameter("Correo", student.getCorreo());
                query.setParameter("Direccion", student.getDireccion());
                query.setParameter("Telefono", student.getTelefono());
                query.setParameter("Carrera", student.getCarrera());
                query.setParameter("Foto", student.getFoto());

                query.execute();
                String mensajeSalida = (String) query.getOutputParameterValue("MensajeSalida");

                if ("El correo ya está registrado.".equals(mensajeSalida)
                                | "El teléfono ya está registrado.".equals(mensajeSalida)) {
                        return ApiResponse.<Student>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                Student studentResponse = Student.builder()
                                .nombres(student.getNombres())
                                .apellidos(student.getApellidos())
                                .correo(student.getCorreo())
                                .direccion(student.getDireccion())
                                .telefono(student.getTelefono())
                                .carrera(student.getCarrera())
                                .foto(student.getFoto())
                                .estado("Activo")
                                .build();

                return ApiResponse.<Student>builder()
                                .data(studentResponse)
                                .message(mensajeSalida)
                                .build();
        }

        @Override
        public ApiResponse<Student> updateStudentService(Student student) {
                StoredProcedureQuery query = entityManager
                                .createStoredProcedureQuery("sp_ActualizarEstudiante")
                                .registerStoredProcedureParameter("IdEstudiante", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Nombres", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Apellidos", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Correo", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Direccion", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Telefono", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Carrera", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Foto", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Estado", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                query.setParameter("IdEstudiante", student.getId());
                query.setParameter("Nombres", student.getNombres());
                query.setParameter("Apellidos", student.getApellidos());
                query.setParameter("Correo", student.getCorreo());
                query.setParameter("Direccion", student.getDireccion());
                query.setParameter("Telefono", student.getTelefono());
                query.setParameter("Carrera", student.getCarrera());
                query.setParameter("Foto", student.getFoto());
                query.setParameter("Estado", student.getEstado());

                query.execute();
                String mensajeSalida = (String) query.getOutputParameterValue("MensajeSalida");

                if ("El estudiante no existe.".equals(mensajeSalida)
                                | "El correo ya está registrado.".equals(mensajeSalida)
                                | "El teléfono ya está registrado.".equals(mensajeSalida)
                                | "El estudiante tiene un préstamo activo y no puede ser inactivado."
                                                .equals(mensajeSalida)) {
                        return ApiResponse.<Student>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                Student studentResponse = Student.builder()
                                .id(student.getId())
                                .nombres(student.getNombres())
                                .apellidos(student.getApellidos())
                                .correo(student.getCorreo())
                                .direccion(student.getDireccion())
                                .telefono(student.getTelefono())
                                .carrera(student.getCarrera())
                                .foto(student.getFoto())
                                .estado(student.getEstado())
                                .build();

                return ApiResponse.<Student>builder()
                                .data(studentResponse)
                                .message(mensajeSalida)
                                .build();
        }

        @Override
        public ApiResponse<Void> deleteStudentService(Integer studentId) {
                StoredProcedureQuery query = entityManager
                                .createStoredProcedureQuery("sp_EliminarEstudiante")
                                .registerStoredProcedureParameter("IdEstudiante", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                query.setParameter("IdEstudiante", studentId);

                query.execute();
                String mensajeSalida = (String) query.getOutputParameterValue("MensajeSalida");

                if ("El estudiante no existe.".equals(mensajeSalida)
                                | "El estudiante tiene préstamos activos y no puede ser eliminado."
                                                .equals(mensajeSalida)) {
                        return ApiResponse.<Void>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                return ApiResponse.<Void>builder()
                                .data(null)
                                .message("Estudiante eliminado.")
                                .build();
        }
}
