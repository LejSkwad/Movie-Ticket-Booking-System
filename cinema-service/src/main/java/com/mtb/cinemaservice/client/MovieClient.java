package com.mtb.cinemaservice.client;

import com.mtb.cinemaservice.dto.response.MovieInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "movie-service")
public interface MovieClient {

    @GetMapping("/internal/movies")
    List<MovieInfo> getMoviesByIds(@RequestParam List<Long> ids);

}
