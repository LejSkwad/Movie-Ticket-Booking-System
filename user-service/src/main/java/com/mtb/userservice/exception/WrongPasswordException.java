package com.mtb.userservice.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Sai mật khẩu hiện tại");
    }

}
