package com.mtb.userservice.mapper;

import com.mtb.userservice.dto.request.CreateUserRequest;
import com.mtb.userservice.dto.response.UserResponse;
import com.mtb.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    User fromCreate(CreateUserRequest createUserRequest);
}
