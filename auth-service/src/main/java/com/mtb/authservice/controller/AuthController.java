package com.mtb.authservice.controller;

import com.mtb.ApiResponse;
import com.mtb.authservice.dto.request.LoginRequest;
import com.mtb.authservice.dto.request.RegisterRequest;
import com.mtb.authservice.dto.response.AuthResponse;
import com.mtb.authservice.dto.response.UserResponse;
import com.mtb.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(ApiResponse.ok(authService.register(registerRequest)));
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(ApiResponse.ok(authService.login(loginRequest)));
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String authHeader){
        authService.logout(authHeader);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
