package com.mtb.cinemaservice.dto.response;

import com.mtb.cinemaservice.entity.SeatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatResponse {
    private Long id;
    private String rowLabel;
    private Integer colNumber;
    private SeatType seatType;
    private Long pairId;
    private BigDecimal price;
}
