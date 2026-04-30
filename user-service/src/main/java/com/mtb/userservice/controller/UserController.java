package com.mtb.userservice.controller;

import com.mtb.ApiResponse;
import com.mtb.userservice.dto.request.*;
import com.mtb.userservice.dto.response.UserResponse;
import com.mtb.userservice.service.UserService;
import jakarta.validation.Valid;
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

    @PutMapping("/api/users/me/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestHeader("X-User-Id") Long id,
                                                            @Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(id, changePasswordRequest);
        return ResponseEntity.ok(ApiResponse.ok(null, "Đổi mật khẩu thành công"));
    }

    @PutMapping("/api/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@RequestHeader("X-User-Id") Long id,
                                                           @Valid @RequestBody UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok(ApiResponse.ok(userService.updateProfile(id, updateUserRequest)));
    }

    //================================ ADMIN ===========================================
    @GetMapping("/api/admin/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(@ModelAttribute SearchUserRequest  searchUserRequest,
                                                                       Pageable pageable){
        return ResponseEntity.ok(ApiResponse.ok(userService.getAllUsers(searchUserRequest,pageable)));
    }

    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(userService.getUser(id)));
    }

    @PutMapping("/api/admin/users/lock-account/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> lockAccount(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(userService.lockAccount(id)));
    }

    @PutMapping("/api/admin/users/unlock-account/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> unlockAccount(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(userService.unlockAccount(id)));
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
