package com.mtb.userservice.controller;

import com.mtb.ApiResponse;
import com.mtb.userservice.dto.request.CreateUserRequest;
import com.mtb.userservice.dto.request.SearchUserRequest;
import com.mtb.userservice.dto.request.ValidateCredentialsRequest;
import com.mtb.userservice.dto.response.UserResponse;
import com.mtb.userservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@RequestHeader("X-User-Id") Long userId){
        return ResponseEntity.ok(ApiResponse.ok(userService.getProfile(userId)));
    }

    //================================ ADMIN ===========================================
    @GetMapping("/api/admin/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(@ModelAttribute SearchUserRequest  searchUserRequest,
                                                                       Pageable pageable){
        return ResponseEntity.ok(ApiResponse.ok(userService.getAllUsers(searchUserRequest,pageable)));
    }

    //================================ INTERNAL =====================================
    @PostMapping("/internal/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createUser(createUserRequest));

    }

    @PostMapping("/internal/users/validate-credentials")
    public ResponseEntity<UserResponse> validateCredentials(@RequestBody ValidateCredentialsRequest request) {
        return ResponseEntity.ok(userService.validateCredentials(request));
    }

}
