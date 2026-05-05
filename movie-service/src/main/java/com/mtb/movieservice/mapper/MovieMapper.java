package com.mtb.movieservice.mapper;

import com.mtb.movieservice.dto.request.CreateMovieRequest;
import com.mtb.movieservice.dto.request.UpdateMovieRequest;
import com.mtb.movieservice.dto.response.MovieResponse;
import com.mtb.movieservice.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieResponse toResponse(Movie movie);

    Movie fromCreate(CreateMovieRequest createMovieRequest);

    void fromUpdate(UpdateMovieRequest updateMovieRequest, @MappingTarget Movie movie);
}
