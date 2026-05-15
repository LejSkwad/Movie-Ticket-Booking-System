package com.mtb.cinemaservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimeResponse {
    private Long id;

    private Long movieId;
    private String movieTitle;
    private Integer movieDurationMin;

    private Long roomId;
    private String roomName;
    private String roomType;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime endTime;

    private String status;
}
