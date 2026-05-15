package com.mtb.movieservice.service;

import com.mtb.movieservice.dto.request.CreateMovieRequest;
import com.mtb.movieservice.dto.request.SearchMovieRequest;
import com.mtb.movieservice.dto.request.UpdateMovieRequest;
import com.mtb.movieservice.dto.response.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    List<MovieResponse> getNowShowing();
    List<MovieResponse> getComingSoon();
    MovieResponse getMovieById(Long id);

    //======================= ADMIN =======================
    Page<MovieResponse> getAllMovies(SearchMovieRequest searchMovieRequest, Pageable pageable);
    MovieResponse createMovie(CreateMovieRequest createMovieRequest);
    MovieResponse updateMovie(Long id, UpdateMovieRequest updateMovieRequest);
    MovieResponse deleteMovie(Long id);

    //====================== INTERNAL =========================
    List<MovieResponse> getMoviesByIds(List<Long> ids);
}
