package com.mtb.movieservice.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMovieRequest {
    private String title;

    private String director;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate releaseDate;

    private String description;

    private String castList;

    private String status;

    private Integer durationMin;

    private String posterUrl;

    private String rated;

    private List<Long> genreIds;
}
