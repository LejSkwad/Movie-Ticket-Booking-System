package com.mtb.movieservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMovieRequest {
    @NotBlank
    private String title;

    private String director;

    private LocalDate releaseDate;

    private String description;

    private String castList;

    @NotBlank
    private String status;

    @NotNull
    private Integer durationMin;

    private String posterUrl;

    private String rated;

    private List<Long> genreIds;
}
