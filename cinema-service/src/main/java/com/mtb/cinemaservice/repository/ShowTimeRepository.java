package com.mtb.cinemaservice.repository;

import com.mtb.cinemaservice.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    //===================== ADMIN =====================
    @Query("SELECT s FROM ShowTime s WHERE DATE(s.startTime) = :date")
    List<ShowTime> getAllByDate(LocalDate date);


    List<ShowTime> getAllByRoomIdAndStartTimeBetween(Long roomId, LocalDateTime startTimeAfter, LocalDateTime startTimeBefore);
}
