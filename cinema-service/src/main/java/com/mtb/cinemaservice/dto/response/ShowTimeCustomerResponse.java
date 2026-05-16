package com.mtb.cinemaservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeCustomerResponse {
    private Long id;
    private String format;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime endTime;
}
