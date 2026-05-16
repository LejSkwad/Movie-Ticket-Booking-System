package com.mtb.cinemaservice.service;

import com.mtb.cinemaservice.dto.response.ShowTimeSeatResponse;

import java.util.List;

public interface ShowTimeSeatService {
    List<ShowTimeSeatResponse> getSeatsByShowTimeId(Long showTimeId);
}
