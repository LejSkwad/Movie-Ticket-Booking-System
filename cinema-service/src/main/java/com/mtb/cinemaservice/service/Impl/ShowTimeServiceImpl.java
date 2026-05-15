package com.mtb.cinemaservice.service.Impl;

import com.mtb.cinemaservice.client.MovieClient;
import com.mtb.cinemaservice.dto.request.CreateShowTimeRequest;
import com.mtb.cinemaservice.dto.response.MovieInfo;
import com.mtb.cinemaservice.dto.response.ShowTimeResponse;
import com.mtb.cinemaservice.entity.*;
import com.mtb.cinemaservice.exception.InvalidShowTimeException;
import com.mtb.cinemaservice.exception.MovieNotFoundException;
import com.mtb.cinemaservice.exception.RoomNotFoundException;
import com.mtb.cinemaservice.exception.ShowTimeConflictException;
import com.mtb.cinemaservice.mapper.ShowTimeMapper;
import com.mtb.cinemaservice.mapper.ShowTimeSeatMapper;
import com.mtb.cinemaservice.repository.RoomRepository;
import com.mtb.cinemaservice.repository.SeatRepository;
import com.mtb.cinemaservice.repository.ShowTimeRepository;
import com.mtb.cinemaservice.repository.ShowTimeSeatRepository;
import com.mtb.cinemaservice.service.ShowTimeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShowTimeServiceImpl implements ShowTimeService {
    private final ShowTimeRepository showTimeRepository;
    private final MovieClient movieClient;
    private final ShowTimeMapper showTimeMapper;
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final ShowTimeSeatMapper showTimeSeatMapper;
    private final ShowTimeSeatRepository showTimeSeatRepository;

    public ShowTimeServiceImpl(ShowTimeRepository showTimeRepository,
                               MovieClient movieClient,
                               ShowTimeMapper showTimeMapper,
                               RoomRepository roomRepository,
                               SeatRepository seatRepository,
                               ShowTimeSeatMapper showTimeSeatMapper, ShowTimeSeatRepository showTimeSeatRepository) {
        this.showTimeRepository = showTimeRepository;
        this.movieClient = movieClient;
        this.showTimeMapper = showTimeMapper;
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
        this.showTimeSeatMapper = showTimeSeatMapper;
        this.showTimeSeatRepository = showTimeSeatRepository;
    }

    @Override
    public List<ShowTimeResponse> getUpcomingShowTimes(LocalDate date, Long movieId) {
        return List.of();
    }

    //======================== ADMIN ========================
    @Override
    public List<ShowTimeResponse> getShowTimesByDate(LocalDate date) {
        List<ShowTime> showTimes = showTimeRepository.getAllByDate(date);

        List<Long> movieIds = showTimes.stream().map(ShowTime::getMovieId).distinct().toList();

        Map<Long, MovieInfo> movieInfoMap = movieClient.getMoviesByIds(movieIds)
                .stream()
                .collect(Collectors.toMap(MovieInfo::getId, Function.identity()));

        List<ShowTimeResponse> showTimeResponses = showTimes.stream().map(s -> {
            MovieInfo movieInfo = movieInfoMap.get(s.getMovieId());
            ShowTimeResponse showTimeResponse = showTimeMapper.toResponse(s, movieInfo);
            showTimeResponse.setStatus(computeStatus(s, LocalDateTime.now()));
            return showTimeResponse;
        })
                .toList();

        return showTimeResponses;
    }

    @Override
    @Transactional
    public List<ShowTimeResponse> createShowTimes(CreateShowTimeRequest createShowTimeRequest) {
        Room room = roomRepository.findById(createShowTimeRequest.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException());

        MovieInfo movieInfo = movieClient.getMoviesByIds(List.of(createShowTimeRequest.getMovieId()))
                .stream().findFirst()
                .orElseThrow(() -> new MovieNotFoundException());

        List<ShowTime> roomShowTimesInDateRange = showTimeRepository.getAllByRoomIdAndStartTimeBetween(createShowTimeRequest.getRoomId(),
                createShowTimeRequest.getStartDate().atStartOfDay(),
                createShowTimeRequest.getEndDate().plusDays(1).atStartOfDay());

        //Kiểm tra xem có showTime trùng
        List<LocalDateTime[]> validShowTime = new ArrayList<>();
        for(LocalDate date = createShowTimeRequest.getStartDate(); !date.isAfter(createShowTimeRequest.getEndDate()); date = date.plusDays(1)) {
            for (String requestStartTime : createShowTimeRequest.getStartTimes()){

                LocalTime time = LocalTime.parse(requestStartTime);
                if (time.isBefore(LocalTime.of(8, 0))) {
                    throw new InvalidShowTimeException("Suất chiếu không hợp lệ: " + requestStartTime);
                }

                LocalDateTime start = LocalDateTime.of(date, time);
                LocalDateTime end = start.plusMinutes(movieInfo.getDurationMin());

                boolean conflictWithDB = roomShowTimesInDateRange.stream()
                        .anyMatch(s -> s.getStartTime().isBefore(end)
                                && s.getEndTime().isAfter(start));

                boolean conflictInBatch = validShowTime.stream()
                        .anyMatch(prev -> prev[0].isBefore(end)
                                && prev[1].isAfter(start));

                if (conflictInBatch || conflictWithDB) {
                    throw new InvalidShowTimeException("Suất chiếu không hợp lệ: " + requestStartTime + " " +  date);
                }

                //Nếu không trùng thì đưa suất chiếu (start,end) vào List hợp lệ
                validShowTime.add(new LocalDateTime[]{start,end});
            }
        }

        List<ShowTime> newShowTimes = validShowTime.stream().map(slot -> {
            ShowTime showTime = showTimeMapper.toEntity(createShowTimeRequest, room, slot[0], slot[1]);
            return showTime;
        })
                .toList();

        List<Seat> seats = seatRepository.findAllByRoomIdOrderByRowLabelAscColNumberAsc(createShowTimeRequest.getRoomId());

        List<ShowTimeSeat> newShowTimeSeats = newShowTimes.stream()
                .flatMap(st -> seats.stream().map(seat -> {
                    ShowTimeSeat showTimeSeat = showTimeSeatMapper.toEntity(st, seat);
                    showTimeSeat.setStatus(ShowTimeSeatStatus.AVAILABLE);
                    return showTimeSeat;
                }))
                .toList();

        List<ShowTime> saved = showTimeRepository.saveAll(newShowTimes);
        showTimeSeatRepository.saveAll(newShowTimeSeats);

        return saved.stream().map(s-> showTimeMapper.toResponse(s, movieInfo)).toList();
    }

    private static String computeStatus(ShowTime showTime, LocalDateTime now){
        if (showTime.getStartTime().isAfter(now))  return "UPCOMING";
        if (showTime.getEndTime().isAfter(now))    return "ONGOING";
        return "FINISHED";
    }
}
