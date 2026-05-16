package com.mtb.cinemaservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeSeatResponse {
    private Long seatId;
    private Character rowLabel;
    private Integer colNumber;
    private String seatType;
    private Long pairId;
    private BigDecimal price;
    private String status;
}
