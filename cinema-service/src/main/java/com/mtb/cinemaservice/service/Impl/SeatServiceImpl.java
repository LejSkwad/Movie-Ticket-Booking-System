package com.mtb.cinemaservice.service.Impl;

import com.mtb.cinemaservice.dto.response.SeatResponse;
import com.mtb.cinemaservice.entity.SeatPriceConfig;
import com.mtb.cinemaservice.entity.SeatType;
import com.mtb.cinemaservice.mapper.SeatMapper;
import com.mtb.cinemaservice.repository.SeatPriceConfigRepository;
import com.mtb.cinemaservice.repository.SeatRepository;
import com.mtb.cinemaservice.service.SeatService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final SeatPriceConfigRepository seatPriceConfigRepository;
    private final SeatMapper seatMapper;

    public SeatServiceImpl(SeatRepository seatRepository,
                           SeatPriceConfigRepository seatPriceConfigRepository,
                           SeatMapper seatMapper) {
        this.seatRepository = seatRepository;
        this.seatPriceConfigRepository = seatPriceConfigRepository;
        this.seatMapper = seatMapper;
    }

    @Override
    public List<SeatResponse> getSeatsByRoomId(Long roomId) {
        Map<SeatType, BigDecimal> priceMap = seatPriceConfigRepository.findAll()
                .stream()
                .collect(Collectors.toMap(SeatPriceConfig::getSeatType, SeatPriceConfig::getPrice));

        return seatRepository.findAllByRoomIdOrderByRowLabelAscColNumberAsc(roomId)
                .stream()
                .map(seat -> {
                    SeatResponse seatResponse = seatMapper.toResponse(seat);
                    seatResponse.setPrice(priceMap.get(seat.getType()));
                    return seatResponse;
                })
                .collect(Collectors.toList());
    }
}
