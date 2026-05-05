package com.mtb.movieservice.controller;

import com.mtb.ApiResponse;
import com.mtb.movieservice.entity.Genre;
import com.mtb.movieservice.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/api/genres")
    public ResponseEntity<ApiResponse<List<Genre>>> getGenres() {
        return ResponseEntity.ok(ApiResponse.ok(genreService.getGenres()));
    }

    @PostMapping("/api/genres")
    public ResponseEntity<ApiResponse<Genre>> createGenre(@RequestBody String name){
        return ResponseEntity.ok(ApiResponse.ok(genreService.createGenre(name)));
    }

    @PutMapping("/api/genres/{id}")
    public ResponseEntity<ApiResponse<Genre>> updateGenre(@PathVariable Long id, @RequestBody String name){
        return ResponseEntity.ok(ApiResponse.ok(genreService.updateGenre(id, name)));
    }

    @DeleteMapping("/api/genres/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGenre(@PathVariable Long id){
        genreService.deleteGenre(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Xóa thể loại thành công"));
    }
}
