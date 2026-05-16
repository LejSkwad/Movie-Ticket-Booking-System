package com.mtb.cinemaservice.repository;

import com.mtb.cinemaservice.entity.ShowTimeSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowTimeSeatRepository extends JpaRepository<ShowTimeSeat, Long> {

    @Query("SELECT ss FROM ShowTimeSeat ss JOIN FETCH ss.seat WHERE ss.showTime.id = :showTimeId ORDER BY ss.seat.rowLabel ASC, ss.seat.colNumber ASC")
    List<ShowTimeSeat> findAllByShowTimeIdWithSeat(Long showTimeId);

    void deleteAllByShowTimeId(Long id);
}
