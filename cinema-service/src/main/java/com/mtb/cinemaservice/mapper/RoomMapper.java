package com.mtb.cinemaservice.mapper;

import com.mtb.cinemaservice.dto.response.RoomResponse;
import com.mtb.cinemaservice.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomResponse toResponse(Room room);
}
