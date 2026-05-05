package com.mtb.movieservice.service;

import com.mtb.movieservice.dto.request.CreateMovieRequest;
import com.mtb.movieservice.dto.request.SearchMovieRequest;
import com.mtb.movieservice.dto.request.UpdateMovieRequest;
import com.mtb.movieservice.dto.response.MovieResponse;
import com.mtb.movieservice.entity.Genre;
import com.mtb.movieservice.entity.Movie;
import com.mtb.movieservice.entity.MovieStatus;
import com.mtb.movieservice.exception.MovieNotFoundException;
import com.mtb.movieservice.mapper.MovieMapper;
import com.mtb.movieservice.repository.GenreRepository;
import com.mtb.movieservice.repository.MovieRepository;
import com.mtb.movieservice.specification.MovieSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreRepository genreRepository;

    public MovieServiceImpl(MovieRepository movieRepository,
                            MovieMapper movieMapper,
                            GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<MovieResponse> getNowShowing() {
        List<Movie> movies = movieRepository.findAllByStatus(MovieStatus.NOW_SHOWING);
        return movies.stream().map(movieMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<MovieResponse> getComingSoon() {
        List<Movie> movies = movieRepository.findAllByStatus(MovieStatus.COMING_SOON);
        return movies.stream().map(movieMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException());
        return movieMapper.toResponse(movie);
    }

    //=============================== ADMIN =============================

    @Override
    public Page<MovieResponse> getAllMovies(SearchMovieRequest searchMovieRequest, Pageable pageable) {
        Specification<Movie> spec = ((root, query, builder) -> builder.conjunction());

        if(searchMovieRequest.getName() != null && !searchMovieRequest.getName().isEmpty()) {
            spec = spec.and(MovieSpecification.hasName(searchMovieRequest.getName()));
        }
        if(searchMovieRequest.getStatus() != null && !searchMovieRequest.getStatus().isEmpty()) {
            spec = spec.and(MovieSpecification.hasStatus(searchMovieRequest.getStatus()));
        }
        if(searchMovieRequest.getGenreIds() != null && !searchMovieRequest.getGenreIds().isEmpty()) {
            spec = spec.and(MovieSpecification.hasAllGenres(searchMovieRequest.getGenreIds()));
        }
        Page<Movie> movies =  movieRepository.findAll(spec, pageable);

        return movies.map(movieMapper::toResponse);
    }

    @Override
    @Transactional
    public MovieResponse createMovie(CreateMovieRequest createMovieRequest) {
        Movie movie = movieMapper.fromCreate(createMovieRequest);

        if(createMovieRequest.getGenreIds() != null && !createMovieRequest.getGenreIds().isEmpty()) {
            List<Genre> genres = genreRepository.findAllById(createMovieRequest.getGenreIds());
            movie.setGenres(genres);
        }

        return movieMapper.toResponse(movieRepository.save(movie));
    }

    @Override
    @Transactional
    public MovieResponse updateMovie(Long id, UpdateMovieRequest updateMovieRequest) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException());

        movieMapper.fromUpdate(updateMovieRequest, movie);
        if (updateMovieRequest.getGenreIds() != null) {
            List<Genre> genres = genreRepository.findAllById(updateMovieRequest.getGenreIds());
            movie.setGenres(genres);
        }

        return movieMapper.toResponse(movieRepository.save(movie));
    }

    @Override
    @Transactional
    public MovieResponse deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException());

        movie.setStatus(MovieStatus.ENDED);

        return movieMapper.toResponse(movieRepository.save(movie));
    }
}
