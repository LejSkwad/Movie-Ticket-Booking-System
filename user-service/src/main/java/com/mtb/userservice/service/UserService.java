package com.mtb.userservice.service;

import com.mtb.userservice.dto.request.*;
import com.mtb.userservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse getProfile(Long id);
    void changePassword(Long id, ChangePasswordRequest changePasswordRequest);
    UserResponse updateProfile(Long id, UpdateUserRequest updateUserRequest);

    //============================== ADMIN ===============================================
    Page<UserResponse> getAllUsers(SearchUserRequest searchUserRequest, Pageable pageable);
    UserResponse getUser(Long id);
    UserResponse lockAccount(Long id);
    UserResponse unlockAccount(Long id);

    //==================================== INTERNAL =======================================
    UserResponse createUser(CreateUserRequest createUserRequest);
    UserResponse validateCredentials(ValidateCredentialsRequest validateCredentialsRequest);

}
