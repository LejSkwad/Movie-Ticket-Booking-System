package com.mtb.cinemaservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mtb.cinemaservice.entity.ShowFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateShowTimeRequest {
    @NotNull
    private Long movieId;

    @NotNull
    private Long roomId;

    @NotBlank
    private String format;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @NotEmpty
    private List<String> startTimes;
}
