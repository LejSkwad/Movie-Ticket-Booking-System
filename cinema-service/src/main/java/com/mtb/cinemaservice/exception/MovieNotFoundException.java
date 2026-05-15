package com.mtb.cinemaservice.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException() {
        super("Phim không tồn tại");
    }
}
