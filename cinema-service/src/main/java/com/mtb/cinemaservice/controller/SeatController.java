package com.mtb.cinemaservice.controller;

import com.mtb.ApiResponse;
import com.mtb.cinemaservice.dto.response.SeatResponse;
import com.mtb.cinemaservice.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService){
        this.seatService = seatService;
    }

    @GetMapping("/api/rooms/{roomId}/seats")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getSeatsByRoomId(@PathVariable Long roomId){
        return ResponseEntity.ok(ApiResponse.ok(seatService.getSeatsByRoomId(roomId)));
    }
}
