package com.mtb.authservice.client;

import com.mtb.authservice.dto.request.LoginRequest;
import com.mtb.authservice.dto.request.RegisterRequest;
import com.mtb.authservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {
    @PostMapping("/internal/users")
    UserResponse createUser(@RequestBody RegisterRequest registerRequest);

    @PostMapping("/internal/users/validate-credentials")
    UserResponse validateCredentials(@RequestBody LoginRequest loginRequest);
}
