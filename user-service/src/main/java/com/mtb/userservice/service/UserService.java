package com.mtb.userservice.service;

import com.mtb.userservice.dto.request.CreateUserRequest;
import com.mtb.userservice.dto.request.SearchUserRequest;
import com.mtb.userservice.dto.request.ValidateCredentialsRequest;
import com.mtb.userservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse getProfile(Long userId);

    //============================== ADMIN ===============================================
    Page<UserResponse> getAllUsers(SearchUserRequest searchUserRequest, Pageable pageable);

    //==================================== INTERNAL =======================================
    UserResponse createUser(CreateUserRequest createUserRequest);
    UserResponse validateCredentials(ValidateCredentialsRequest validateCredentialsRequest);

}
