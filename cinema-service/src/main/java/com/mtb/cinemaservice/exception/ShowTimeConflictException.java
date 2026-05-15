package com.mtb.cinemaservice.exception;

public class ShowTimeConflictException extends RuntimeException {
    public ShowTimeConflictException(String requestStartTime){
        super("Suất chiếu không hợp lệ: " + requestStartTime);
    }
}
