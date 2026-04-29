package com.mtb.authservice.service;

import com.mtb.authservice.client.UserClient;
import com.mtb.authservice.dto.request.LoginRequest;
import com.mtb.authservice.dto.request.RegisterRequest;
import com.mtb.authservice.dto.response.AuthResponse;
import com.mtb.authservice.dto.response.UserResponse;
import com.mtb.authservice.util.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserClient userClient;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public AuthServiceImpl(UserClient userClient,
                           JwtUtil jwtUtil,
                           StringRedisTemplate stringRedisTemplate) {
        this.userClient = userClient;;
        this.jwtUtil = jwtUtil;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        UserResponse user = userClient.createUser(registerRequest);
        return user;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        UserResponse user = userClient.validateCredentials(loginRequest);
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        return new AuthResponse(token, user);
    }

    @Override
    public void logout(String authHeader) {
        String token = authHeader.substring(7);
        long remainingTime = jwtUtil.getRemainingExpireTime(token);
        if (remainingTime > 0) {
            stringRedisTemplate.opsForValue().set("blacklist:" + token, "1", remainingTime, TimeUnit.MILLISECONDS);
        }
    }
}
