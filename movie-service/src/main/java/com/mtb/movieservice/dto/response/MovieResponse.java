package com.mtb.movieservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {
    Long id;
    String title;
    String description;
    Integer durationMin;
    String director;
    String castList;
    String posterUrl;
    String rated;
    String status;
    List<GenreResponse> genres;
    LocalDate releaseDate;

}
