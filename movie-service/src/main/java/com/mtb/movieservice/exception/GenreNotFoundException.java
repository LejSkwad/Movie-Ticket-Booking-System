package com.mtb.movieservice.exception;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException() {
        super("Không tìm thấy thể loại");
    }
}
