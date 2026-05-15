package com.mtb.cinemaservice.service;

import com.mtb.cinemaservice.dto.request.CreateShowTimeRequest;
import com.mtb.cinemaservice.dto.response.ShowTimeResponse;

import java.time.LocalDate;
import java.util.List;

public interface ShowTimeService {
    List<ShowTimeResponse> getUpcomingShowTimes(LocalDate date, Long movieId);

    //========================= ADMIN ==========================
    List<ShowTimeResponse> getShowTimesByDate(LocalDate date);
    List<ShowTimeResponse> createShowTimes(CreateShowTimeRequest createShowTimeRequest);
}
