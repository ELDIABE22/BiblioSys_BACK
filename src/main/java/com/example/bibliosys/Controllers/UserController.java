package com.example.bibliosys.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliosys.Models.request.user.UserCreateRequest;
import com.example.bibliosys.Models.request.user.UserUpdateRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.user.UserResponse;
import com.example.bibliosys.Services.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/user")
    public ResponseEntity<List<UserResponse>> fetchAllUsersController() {
        List<UserResponse> users = userServiceImpl.fetchAllUsersService();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/user/new")
    public ResponseEntity<ApiResponse<UserResponse>> createUserController(@RequestBody UserCreateRequest userRequest) {
        ApiResponse<UserResponse> apiResponse = userServiceImpl.newUserService(userRequest);

        HttpStatus status = "Usuario registrado".equals(apiResponse.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @PutMapping("/user/update")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserController(
            @RequestBody UserUpdateRequest userRequest) {
        ApiResponse<UserResponse> apiResponse = userServiceImpl.updateUserService(userRequest);

        HttpStatus status = "Usuario actualizado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserController(@PathVariable Integer userId) {
        ApiResponse<Void> apiResponse = userServiceImpl.deleteUserService(userId);

        HttpStatus status = "Usuario eliminado.".equals(apiResponse.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(apiResponse, status);
    }
}
