package com.mtb.userservice.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super("Email đã tồn tại");
    }
}
