package com.mtb.cinemaservice.repository;

import com.mtb.cinemaservice.entity.ShowTimeSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowTimeSeatRepository extends JpaRepository<ShowTimeSeat, Long> {
}
