package com.mtb.cinemaservice.controller;

import com.mtb.ApiResponse;
import com.mtb.cinemaservice.dto.request.CreateShowTimeRequest;
import com.mtb.cinemaservice.dto.response.ShowTimeResponse;
import com.mtb.cinemaservice.service.ShowTimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    public ShowTimeController(ShowTimeService showTimeService){
        this.showTimeService = showTimeService;
    }

    // Customer — chỉ thấy UPCOMING
    @GetMapping("/api/showtimes")
    public ResponseEntity<ApiResponse<List<ShowTimeResponse>>> getShowTimes(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date,
            @RequestParam(required = false) Long movieId) {
        return ResponseEntity.ok(ApiResponse.ok(showTimeService.getUpcomingShowTimes(date, movieId)));
    }

    //================================ ADMIN ===========================================

    // Admin — thấy tất cả (UPCOMING / ONGOING / FINISHED)
    @GetMapping("/api/admin/showtimes")
    public ResponseEntity<ApiResponse<List<ShowTimeResponse>>> getAdminShowTimes(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        return ResponseEntity.ok(ApiResponse.ok(showTimeService.getShowTimesByDate(date)));
    }

    @PostMapping("/api/admin/showtimes")
    public ResponseEntity<ApiResponse<List<ShowTimeResponse>>> createShowTimes(@RequestBody CreateShowTimeRequest createShowTimeRequest){
        return ResponseEntity.ok(ApiResponse.ok(showTimeService.createShowTimes(createShowTimeRequest)));
    }

}
