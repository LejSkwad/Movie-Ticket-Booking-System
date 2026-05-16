package com.mtb.movieservice.repository;

import com.mtb.movieservice.entity.Movie;
import com.mtb.movieservice.entity.MovieStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres WHERE m.status = :status")
    List<Movie> findAllByStatus(MovieStatus status);

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres WHERE m.id IN :ids")
    List<Movie> findAllWithGenresByIds(List<Long> ids);

    @EntityGraph(attributePaths = "genres")
    Page<Movie> findAll(Specification<Movie> spec, Pageable pageable);
}
