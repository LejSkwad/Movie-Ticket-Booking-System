package com.mtb.userservice.service;

import com.mtb.userservice.dto.request.CreateUserRequest;
import com.mtb.userservice.dto.request.SearchUserRequest;
import com.mtb.userservice.dto.request.ValidateCredentialsRequest;
import com.mtb.userservice.dto.response.UserResponse;
import com.mtb.userservice.entity.Role;
import com.mtb.userservice.entity.User;
import com.mtb.userservice.exception.EmailAlreadyExistException;
import com.mtb.userservice.exception.InvalidCredentialsException;
import com.mtb.userservice.exception.UserBannedException;
import com.mtb.userservice.exception.UserNotFoundException;
import com.mtb.userservice.mapper.UserMapper;
import com.mtb.userservice.repository.UserRepository;
import com.mtb.userservice.specification.UserSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        if(!user.getIsActive()){
            throw new UserBannedException();
        }

        return userMapper.toResponse(user);
    }

    //================================== ADMIN =========================================

    @Override
    public Page<UserResponse> getAllUsers(SearchUserRequest searchUserRequest, Pageable pageable) {
        Specification<User> spec = (root, query, builder) -> builder.conjunction();
        if(searchUserRequest.getSearch() != null && !searchUserRequest.getSearch().isEmpty()){
            spec = spec.and(UserSpecification.hasSearch(searchUserRequest.getSearch()));
        }
        if(searchUserRequest.getRole() != null && !searchUserRequest.getRole().isEmpty()){
            spec = spec.and(UserSpecification.hasRole(searchUserRequest.getRole()));
        }
        if(searchUserRequest.getIsActive() != null){
            spec = spec.and(UserSpecification.hasStatus(searchUserRequest.getIsActive()));
        }
        if(searchUserRequest.getCreatedFrom() != null || searchUserRequest.getCreatedTo() != null){
            spec = spec.and(UserSpecification.createdBetween(searchUserRequest.getCreatedFrom(), searchUserRequest.getCreatedTo()));
        }

        Page<User> users = userRepository.findAll(spec, pageable);
        Page<UserResponse> responsePage = users.map(userMapper::toResponse);

        return responsePage;
    }

    //=================================== INTERNAL ===========================================

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        if(userRepository.existsByEmail(createUserRequest.getEmail())){
            throw new EmailAlreadyExistException();
        }

        User user = userMapper.fromCreate(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse validateCredentials(ValidateCredentialsRequest validateCredentialsRequest) {
        User user = userRepository.findByEmail(validateCredentialsRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException());
        if(!user.getIsActive()){
            throw new UserBannedException();
        }
        if(!passwordEncoder.matches(validateCredentialsRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException();
        }

        return userMapper.toResponse(user);
    }
}
