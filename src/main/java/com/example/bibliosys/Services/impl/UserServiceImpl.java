package com.example.bibliosys.Services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.bibliosys.Models.User;
import com.example.bibliosys.Models.request.user.UserCreateRequest;
import com.example.bibliosys.Models.request.user.UserUpdateRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.user.UserResponse;
import com.example.bibliosys.Repository.UserRepository;
import com.example.bibliosys.Services.EmailService;
import com.example.bibliosys.Services.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

@Service
public class UserServiceImpl implements UserService {
        @Autowired
        private EntityManager entityManager;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private EmailService emailService;

        @Value("${app.frontend.url}")
        private String frontendUrl;

        @Override
        public List<UserResponse> fetchAllUsersService() {
                return ((List<User>) userRepository.findAll()).stream()
                                .map(user -> UserResponse.builder()
                                                .id(user.getId())
                                                .nombres(user.getNombres())
                                                .apellidos(user.getApellidos())
                                                .usuario(user.getUsuario())
                                                .correo(user.getCorreo())
                                                .rol(user.getRol())
                                                .estado(user.getEstado())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Override
        public ApiResponse<Void> deleteUserService(Integer userId) {
                StoredProcedureQuery deleteUserQuery = entityManager.createStoredProcedureQuery("sp_EliminarUsuario")
                                .registerStoredProcedureParameter("Id", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                deleteUserQuery.setParameter("Id", userId);

                deleteUserQuery.execute();
                String mensajeSalida = (String) deleteUserQuery.getOutputParameterValue("MensajeSalida");

                if ("El usuario no existe.".equals(mensajeSalida)) {
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
        public ApiResponse<UserResponse> newUserService(UserCreateRequest userRequest) {
                StoredProcedureQuery registerUserQuery = entityManager.createStoredProcedureQuery("sp_InsertarUsuario")
                                .registerStoredProcedureParameter("Nombres", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Apellidos", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Correo", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("rol", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                registerUserQuery.setParameter("Nombres", userRequest.getNombres());
                registerUserQuery.setParameter("Apellidos", userRequest.getApellidos());
                registerUserQuery.setParameter("Usuario", userRequest.getUsuario());
                registerUserQuery.setParameter("Correo", userRequest.getCorreo());
                registerUserQuery.setParameter("rol", userRequest.getRol());

                registerUserQuery.execute();
                String mensajeSalida = (String) registerUserQuery.getOutputParameterValue("MensajeSalida");

                if ("El usuario ya está registrado.".equals(mensajeSalida)) {
                        return ApiResponse.<UserResponse>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                if ("El correo ya está registrado.".equals(mensajeSalida)) {
                        return ApiResponse.<UserResponse>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                UserResponse userResponse = UserResponse.builder()
                                .nombres(userRequest.getNombres())
                                .apellidos(userRequest.getApellidos())
                                .usuario(userRequest.getUsuario())
                                .correo(userRequest.getCorreo())
                                .rol(userRequest.getRol())
                                .estado("Activo")
                                .build();

                String resetLink = frontendUrl + "/reset-password?email=" + userRequest.getCorreo();
                String emailContent = emailService.buildEmailContent(userRequest.getNombres(),
                                "Bienvenido a BiblioSys. Por favor establece tu contraseña usando el siguiente enlace:",
                                resetLink);
                emailService.sendSimpleMessage(userRequest.getCorreo(), "Bienvenido a BiblioSys", emailContent);

                return ApiResponse.<UserResponse>builder()
                                .data(userResponse)
                                .message(mensajeSalida)
                                .build();
        }

        @Override
        public ApiResponse<UserResponse> updateUserService(UserUpdateRequest studentRequest) {
                StoredProcedureQuery updateUserQuery = entityManager.createStoredProcedureQuery("sp_ActualizarUsuario")
                                .registerStoredProcedureParameter("Id", Integer.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Nombres", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Apellidos", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Usuario", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Correo", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Rol", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("Estado", String.class, ParameterMode.IN)
                                .registerStoredProcedureParameter("MensajeSalida", String.class, ParameterMode.OUT);

                updateUserQuery.setParameter("Id", studentRequest.getId());
                updateUserQuery.setParameter("Nombres", studentRequest.getNombres());
                updateUserQuery.setParameter("Apellidos", studentRequest.getApellidos());
                updateUserQuery.setParameter("Usuario", studentRequest.getUsuario());
                updateUserQuery.setParameter("Correo", studentRequest.getCorreo());
                updateUserQuery.setParameter("Rol", studentRequest.getRol());
                updateUserQuery.setParameter("Estado", studentRequest.getEstado());

                updateUserQuery.execute();
                String mensajeSalida = (String) updateUserQuery.getOutputParameterValue("MensajeSalida");

                if ("El usuario no existe.".equals(mensajeSalida) ||
                                "El nombre de usuario ya está en uso.".equals(mensajeSalida)) {
                        return ApiResponse.<UserResponse>builder()
                                        .data(null)
                                        .message(mensajeSalida)
                                        .build();
                }

                UserResponse userResponse = UserResponse.builder()
                                .id(studentRequest.getId())
                                .nombres(studentRequest.getNombres())
                                .apellidos(studentRequest.getApellidos())
                                .usuario(studentRequest.getUsuario())
                                .correo(studentRequest.getCorreo())
                                .rol(studentRequest.getRol())
                                .estado(studentRequest.getEstado())
                                .build();

                return ApiResponse.<UserResponse>builder()
                                .data(userResponse)
                                .message(mensajeSalida)
                                .build();
        }

}
