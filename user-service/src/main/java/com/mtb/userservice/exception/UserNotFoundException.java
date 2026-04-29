package com.mtb.userservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Không tìm thấy người dùng");
    }
}
