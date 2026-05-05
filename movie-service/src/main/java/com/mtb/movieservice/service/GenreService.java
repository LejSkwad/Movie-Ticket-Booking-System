package com.mtb.movieservice.service;

import com.mtb.movieservice.entity.Genre;

import java.util.List;

public interface GenreService {
    List<Genre>  getGenres();
    Genre createGenre(String name);
    Genre updateGenre(Long id, String name);
    void deleteGenre(Long id);
}
