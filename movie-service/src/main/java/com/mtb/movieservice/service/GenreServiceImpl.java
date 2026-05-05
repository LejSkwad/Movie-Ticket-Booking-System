package com.mtb.movieservice.service;

import com.mtb.movieservice.entity.Genre;
import com.mtb.movieservice.exception.GenreAlreadyExistException;
import com.mtb.movieservice.exception.GenreNotFoundException;
import com.mtb.movieservice.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getGenres() {
        return  genreRepository.findAll();
    }

    @Override
    @Transactional
    public Genre createGenre(String name) {
        if(genreRepository.existsByNameContainingIgnoreCase(name)){
           throw new GenreAlreadyExistException();
        }

        Genre genre = new Genre();
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public Genre updateGenre(Long id, String name) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException());
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException());
        genreRepository.delete(genre);
    }
}
