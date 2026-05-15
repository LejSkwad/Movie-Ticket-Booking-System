package com.mtb.cinemaservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfo {
    private Long id;
    private String title;
    private Integer durationMin;
}