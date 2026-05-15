package com.mtb.cinemaservice.mapper;

import com.mtb.cinemaservice.entity.Seat;
import com.mtb.cinemaservice.entity.ShowTime;
import com.mtb.cinemaservice.entity.ShowTimeSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowTimeSeatMapper {

    @Mapping(target = "id", ignore = true)
    ShowTimeSeat toEntity(ShowTime showTime, Seat seat);
}
