package com.mtb.authservice.service;

import com.mtb.authservice.dto.request.LoginRequest;
import com.mtb.authservice.dto.request.RegisterRequest;
import com.mtb.authservice.dto.response.AuthResponse;
import com.mtb.authservice.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    void logout(String authHeader);
}
