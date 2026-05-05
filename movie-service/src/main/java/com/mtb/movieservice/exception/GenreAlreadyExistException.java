package com.mtb.movieservice.exception;

public class GenreAlreadyExistException extends RuntimeException {
    public GenreAlreadyExistException() {
        super("Thể loại đã tồn tại");
    }
}
