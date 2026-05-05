package com.mtb.movieservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMovieRequest {
    String name;
    String status;
    List<Long> genreIds;
}
