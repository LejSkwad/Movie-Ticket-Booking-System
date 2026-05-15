package com.mtb.cinemaservice.mapper;

import com.mtb.cinemaservice.dto.response.SeatResponse;
import com.mtb.cinemaservice.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(source = "type", target = "seatType")
    SeatResponse toResponse(Seat seat);
}
