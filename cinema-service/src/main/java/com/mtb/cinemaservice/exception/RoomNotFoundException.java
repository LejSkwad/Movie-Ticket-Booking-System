package com.mtb.cinemaservice.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException() {
        super("Rạp phim không tồn tại");
    }
}
