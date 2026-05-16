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
        if (!showTimeRepository.existsById(showTimeId)) {
            throw new ShowTimeNotFoundException();
        }

        Map<SeatType, BigDecimal> priceMap = seatPriceConfigRepository.findAll()
                .stream()
                .collect(Collectors.toMap(SeatPriceConfig::getSeatType, SeatPriceConfig::getPrice));

        return showTimeSeatRepository.findAllByShowTimeIdWithSeat(showTimeId)
                .stream()
                .map(ss -> toResponse(ss, priceMap))
                .collect(Collectors.toList());
    }

    private ShowTimeSeatResponse toResponse(ShowTimeSeat ss, Map<SeatType, BigDecimal> priceMap) {
        ShowTimeSeatResponse res = new ShowTimeSeatResponse();
        res.setSeatId(ss.getSeat().getId());
        res.setRowLabel(ss.getSeat().getRowLabel());
        res.setColNumber(ss.getSeat().getColNumber());
        res.setSeatType(ss.getSeat().getType().name());
        res.setPairId(ss.getSeat().getPairId());
        res.setPrice(priceMap.get(ss.getSeat().getType()));
        res.setStatus(ss.getStatus().name());
        return res;
    }
}
