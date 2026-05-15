package com.mtb.cinemaservice.repository;

import com.mtb.cinemaservice.entity.SeatPriceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatPriceConfigRepository extends JpaRepository<SeatPriceConfig, Long> {
}
