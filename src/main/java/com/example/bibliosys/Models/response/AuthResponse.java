package com.example.bibliosys.Models.response;

import com.example.bibliosys.Models.User;

public record AuthResponse(
                User user,
                String token) {
}
