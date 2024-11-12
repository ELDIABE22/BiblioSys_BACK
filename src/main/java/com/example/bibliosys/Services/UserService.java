package com.example.bibliosys.Services;

import java.util.List;

import com.example.bibliosys.Models.request.user.UserCreateRequest;
import com.example.bibliosys.Models.request.user.UserUpdateRequest;
import com.example.bibliosys.Models.response.ApiResponse;
import com.example.bibliosys.Models.response.user.UserResponse;

public interface UserService {
    List<UserResponse> fetchAllUsersService();

    ApiResponse<UserResponse> newUserService(UserCreateRequest studentRequest);

    ApiResponse<UserResponse> updateUserService(UserUpdateRequest studentRequest);

    ApiResponse<Void> deleteUserService(Integer userId);
}
