package com.mtb.cinemaservice.controller;

import com.mtb.ApiResponse;
import com.mtb.cinemaservice.dto.request.CreateShowTimeRequest;
import com.mtb.cinemaservice.dto.request.GetUpComingShowTimeRequest;
import com.mtb.cinemaservice.dto.response.ShowTimeCustomerResponse;
import com.mtb.cinemaservice.dto.response.ShowTimeResponse;
import com.mtb.cinemaservice.dto.response.ShowTimeSeatResponse;
import com.mtb.cinemaservice.service.ShowTimeSeatService;
import com.mtb.cinemaservice.service.ShowTimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ShowTimeController {
    private final ShowTimeService showTimeService;
    private final ShowTimeSeatService showTimeSeatService;

    public ShowTimeController(ShowTimeService showTimeService, ShowTimeSeatService showTimeSeatService) {
        this.showTimeService = showTimeService;
        this.showTimeSeatService = showTimeSeatService;
    }

    @GetMapping("/api/showtimes")
    public ResponseEntity<ApiResponse<List<ShowTimeCustomerResponse>>> getShowTimes(@ModelAttribute GetUpComingShowTimeRequest getUpComingShowTimeRequest) {
        return ResponseEntity.ok(ApiResponse.ok(showTimeService.getUpcomingShowTimes(getUpComingShowTimeRequest)));
    }

    @GetMapping("/api/showtimes/{id}/seats")
    public ResponseEntity<ApiResponse<List<ShowTimeSeatResponse>>> getShowTimeSeats(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(showTimeSeatService.getSeatsByShowTimeId(id)));
    }

    //================================ ADMIN ===========================================
    @GetMapping("/api/admin/showtimes")
    public ResponseEntity<ApiResponse<List<ShowTimeResponse>>> getAdminShowTimes(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        return ResponseEntity.ok(ApiResponse.ok(showTimeService.getShowTimesByDate(date)));
    }

    @PostMapping("/api/admin/showtimes")
    public ResponseEntity<ApiResponse<List<ShowTimeResponse>>> createShowTimes(@RequestBody CreateShowTimeRequest createShowTimeRequest) {
        return ResponseEntity.ok(ApiResponse.ok(showTimeService.createShowTimes(createShowTimeRequest)));
    }

    @DeleteMapping("/api/admin/showtimes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShowTime(@PathVariable Long id) {
        showTimeService.deleteShowTime(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Xóa suất chiếu thành công"));
    }
}
