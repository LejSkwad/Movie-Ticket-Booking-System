package com.mtb.cinemaservice.exception;

public class ShowTimeNotFoundException extends RuntimeException {
    public ShowTimeNotFoundException(){
        super("Suất chiếu không tồn tại");
    }
}
