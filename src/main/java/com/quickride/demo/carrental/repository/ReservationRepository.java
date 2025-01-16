package com.quickride.demo.carrental.repository;

import com.quickride.demo.carrental.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    List<Reservation> findByAppUserId(String userId);
}
