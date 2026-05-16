package com.mtb.cinemaservice.repository;

import com.mtb.cinemaservice.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    //===================== ADMIN =====================
    @Query("SELECT s FROM ShowTime s JOIN FETCH s.room WHERE DATE(s.startTime) = :date")
    List<ShowTime> findAllByDate(LocalDate date);


    List<ShowTime> findAllByRoomIdAndStartTimeBetween(Long roomId, LocalDateTime startTimeAfter, LocalDateTime startTimeBefore);

    @Query("SELECT s FROM ShowTime s JOIN FETCH s.showTimeSeats WHERE s.id = :id")
    Optional<ShowTime> findByIdWithSeats(Long id);

    @Query("SELECT s FROM ShowTime s WHERE s.movieId = :movieId AND DATE(s.startTime) = :date AND s.startTime > CURRENT_TIMESTAMP")
    List<ShowTime> findUpComingByMovieIdAndDate(Long movieId, LocalDate date);
}
