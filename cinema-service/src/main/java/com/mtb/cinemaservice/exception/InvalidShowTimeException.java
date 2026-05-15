package com.mtb.cinemaservice.exception;

public class InvalidShowTimeException extends RuntimeException {
    public InvalidShowTimeException(String message) {
        super(message);
    }
}
