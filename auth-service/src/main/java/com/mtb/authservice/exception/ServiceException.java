package com.mtb.authservice.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final int status;  // HTTP status code
    private final String errorCode; // error code như "INVALID_CREDENTIALS",......

    public ServiceException(int status, String errorCode, String message) {
        super(message); // message truyền lên RuntimeException
        this.status = status;
        this.errorCode = errorCode;
    }
}
