package com.mtb.movieservice.controller;

import com.mtb.ApiResponse;
import com.mtb.movieservice.dto.request.CreateMovieRequest;
import com.mtb.movieservice.dto.request.SearchMovieRequest;
import com.mtb.movieservice.dto.request.UpdateMovieRequest;
import com.mtb.movieservice.dto.response.MovieResponse;
import com.mtb.movieservice.service.FileStorageService;
import com.mtb.movieservice.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class MovieController {
    private final MovieService movieService;
    private final FileStorageService fileStorageService;

    public MovieController(MovieService movieService, FileStorageService fileStorageService) {
        this.movieService = movieService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/api/movies/now-showing")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getNowShowing(){
        return ResponseEntity.ok(ApiResponse.ok(movieService.getNowShowing()));
    }

    @GetMapping("/api/movies/coming-soon")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> comingSoon(){
        return ResponseEntity.ok(ApiResponse.ok(movieService.getComingSoon()));
    }

    @GetMapping("/api/movies/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(movieService.getMovieById(id)));
    }

    //============================== ADMIN ============================

    @GetMapping("/api/admin/movies")
    public ResponseEntity<ApiResponse<Page<MovieResponse>>> getAllMovies(@ModelAttribute SearchMovieRequest searchMovieRequest,
                                                                         Pageable pageable){
        return ResponseEntity.ok(ApiResponse.ok(movieService.getAllMovies(searchMovieRequest,pageable)));
    }

    @PostMapping("/api/admin/movies")
    public ResponseEntity<ApiResponse<MovieResponse>> createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest){
        return ResponseEntity.ok(ApiResponse.ok(movieService.createMovie(createMovieRequest)));
    }

    @PostMapping("/api/admin/movies/upload-poster")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadPoster(
            @RequestParam("file") MultipartFile file) {
        String url = fileStorageService.uploadPoster(file);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("url", url)));
    }

    @PutMapping("/api/admin/movies/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> updateMovie(@PathVariable Long id,
                                                                  @RequestBody UpdateMovieRequest updateMovieRequest){
        return ResponseEntity.ok(ApiResponse.ok(movieService.updateMovie(id, updateMovieRequest)));
    }

    @DeleteMapping("/api/admin/movies/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> deleteMovie(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.ok(movieService.deleteMovie(id)));
    }
}
