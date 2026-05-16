package com.mtb.cinemaservice.controller;

import com.mtb.ApiResponse;
import com.mtb.cinemaservice.dto.response.RoomResponse;
import com.mtb.cinemaservice.dto.response.SeatResponse;
import com.mtb.cinemaservice.service.RoomService;
import com.mtb.cinemaservice.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomController {
    private final RoomService roomService;
    private final SeatService seatService;

    public RoomController(RoomService roomService, SeatService seatService) {
        this.roomService = roomService;
        this.seatService = seatService;
    }

    @GetMapping("/api/admin/rooms")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getAllRooms() {
        return ResponseEntity.ok(ApiResponse.ok(roomService.getAllRooms()));
    }

    @GetMapping("/api/admin/rooms/{roomId}/seats")
    public ResponseEntity<ApiResponse<List<SeatResponse>>> getSeatsByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(ApiResponse.ok(seatService.getSeatsByRoomId(roomId)));
    }
}
