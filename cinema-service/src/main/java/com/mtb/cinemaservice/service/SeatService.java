package com.mtb.cinemaservice.service;

import com.mtb.cinemaservice.dto.response.SeatResponse;

import java.util.List;

public interface SeatService {
    List<SeatResponse> getSeatsByRoomId(Long roomId);
}
