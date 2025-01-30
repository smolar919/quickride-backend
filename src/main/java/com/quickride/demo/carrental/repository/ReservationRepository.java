package com.quickride.demo.carrental.repository;

import com.quickride.demo.carrental.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    List<Reservation> findByAppUserId(String userId);
    @Query("SELECT r FROM Reservation r WHERE r.car.id = :carId " +
            "AND ((r.startDate <= :endDate AND r.endDate >= :startDate))")
    List<Reservation> findOverlappingReservations(
            @Param("carId") String carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM Reservation r WHERE r.car.id = :carId")
    List<Reservation> findReservationsByCarId(@Param("carId") String carId);
}

