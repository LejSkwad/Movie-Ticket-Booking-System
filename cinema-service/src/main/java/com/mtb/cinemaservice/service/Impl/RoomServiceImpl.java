package com.mtb.cinemaservice.service.Impl;

import com.mtb.cinemaservice.dto.response.RoomResponse;
import com.mtb.cinemaservice.entity.Room;
import com.mtb.cinemaservice.mapper.RoomMapper;
import com.mtb.cinemaservice.repository.RoomRepository;
import com.mtb.cinemaservice.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomServiceImpl(RoomRepository roomRepository,
                           RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> roomResponses = rooms.stream().map(roomMapper::toResponse).collect(Collectors.toList());
        return roomResponses;
    }
}
