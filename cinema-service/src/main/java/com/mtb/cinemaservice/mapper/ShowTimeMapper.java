package com.mtb.cinemaservice.mapper;

import com.mtb.cinemaservice.dto.request.CreateShowTimeRequest;
import com.mtb.cinemaservice.dto.response.MovieInfo;
import com.mtb.cinemaservice.dto.response.ShowTimeCustomerResponse;
import com.mtb.cinemaservice.dto.response.ShowTimeResponse;
import com.mtb.cinemaservice.entity.Room;
import com.mtb.cinemaservice.entity.ShowTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ShowTimeMapper {

    ShowTimeCustomerResponse toCustomerResponse(ShowTime showTime);

    @Mapping(source = "showTime.id", target = "id")
    @Mapping(source = "showTime.room.id",   target = "roomId")
    @Mapping(source = "showTime.room.name", target = "roomName")
    @Mapping(source = "showTime.room.type", target = "roomType")
    @Mapping(source = "movieInfo.title",      target = "movieTitle")
    @Mapping(source = "movieInfo.durationMin", target ="movieDurationMin")
    ShowTimeResponse toResponse(ShowTime showTime, MovieInfo movieInfo);

    @Mapping(target = "id", ignore = true)
    ShowTime toEntity(CreateShowTimeRequest createShowTimeRequest, Room room, LocalDateTime startTime, LocalDateTime endTime);
}
