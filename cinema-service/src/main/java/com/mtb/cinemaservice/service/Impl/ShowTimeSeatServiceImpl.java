package com.mtb.cinemaservice.service.Impl;

import com.mtb.cinemaservice.dto.response.ShowTimeSeatResponse;
import com.mtb.cinemaservice.entity.SeatPriceConfig;
import com.mtb.cinemaservice.entity.SeatType;
import com.mtb.cinemaservice.entity.ShowTimeSeat;
import com.mtb.cinemaservice.exception.ShowTimeNotFoundException;
import com.mtb.cinemaservice.repository.SeatPriceConfigRepository;
import com.mtb.cinemaservice.repository.ShowTimeSeatRepository;
import com.mtb.cinemaservice.repository.ShowTimeRepository;
import com.mtb.cinemaservice.service.ShowTimeSeatService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShowTimeSeatServiceImpl implements ShowTimeSeatService {
    private final ShowTimeSeatRepository showTimeSeatRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SeatPriceConfigRepository seatPriceConfigRepository;

    public ShowTimeSeatServiceImpl(ShowTimeSeatRepository showTimeSeatRepository,
                                   ShowTimeRepository showTimeRepository,
                                   SeatPriceConfigRepository seatPriceConfigRepository) {
        this.showTimeSeatRepository = showTimeSeatRepository;
        this.showTimeRepository = showTimeRepository;
        this.seatPriceConfigRepository = seatPriceConfigRepository;
    }

    @Override
    public List<ShowTimeSeatResponse> getSeatsByShowTimeId(Long showTimeId) {

        return List.of();
    }
}
