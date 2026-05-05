package com.mtb.movieservice.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException() {
        super("Không tìm thấy phim");
    }
}
