package com.example.bibliosys.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliosys.Models.request.AuthCreateUserRequest;
import com.example.bibliosys.Models.request.AuthLoginRequest;
import com.example.bibliosys.Models.request.ResetPasswordRequest;
import com.example.bibliosys.Models.request.user.UserCreateRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.AuthResponse;
import com.example.bibliosys.Services.impl.UserDetailServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerController(
            @RequestBody @Valid AuthCreateUserRequest userRequest) {
        ApiResponse<AuthResponse> response = userDetailService.registerService(userRequest);

        HttpStatus status = "Usuario creado".equals(response.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginController(@RequestBody @Valid AuthLoginRequest userRequest) {
        ApiResponse<AuthResponse> response = userDetailService.loginService(userRequest);

        if (response.getData() != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean isUpdated = userDetailService.updatePassword(resetPasswordRequest.getEmail(),
                resetPasswordRequest.getPassword());
        if (isUpdated) {
            return ResponseEntity.ok("Contraseña actualizada con éxito");
        } else {
            return ResponseEntity.status(400).body("Error al actualizar la contraseña");
        }
    }

    @PostMapping("/link-reset-password")
    public ResponseEntity<ApiResponse<Void>> sendMailLinkResetPassword(@RequestBody UserCreateRequest userRequest) {
        ApiResponse<Void> response = userDetailService.sendEmailResetPassword(userRequest);

        HttpStatus status = "Correo de restablecimiento de contraseña enviado exitosamente"
                .equals(response.getMessage())
                        ? HttpStatus.OK
                        : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

}
