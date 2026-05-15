package com.mtb.cinemaservice.service;

import com.mtb.cinemaservice.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {
    List<RoomResponse> getAllRooms();
}
