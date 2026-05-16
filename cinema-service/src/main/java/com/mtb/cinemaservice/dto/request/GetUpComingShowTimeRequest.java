package com.mtb.cinemaservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUpComingShowTimeRequest {
    private Long movieId;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
}
