package com.mtb.cinemaservice.repository;

import com.mtb.cinemaservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByRoomIdOrderByRowLabelAscColNumberAsc(Long roomId);
}
