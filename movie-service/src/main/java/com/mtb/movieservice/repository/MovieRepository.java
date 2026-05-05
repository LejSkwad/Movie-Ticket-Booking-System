package com.mtb.movieservice.repository;

import com.mtb.movieservice.entity.Movie;
import com.mtb.movieservice.entity.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
     List<Movie> findAllByStatus(MovieStatus status);
}
