package com.quickride.demo.carrental.service;

import com.quickride.demo.carrental.model.Reservation;
import com.quickride.demo.carrental.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByAppUserId(String userId) {
        return reservationRepository.findByAppUserId(userId);
    }

    public Reservation getReservationById(String id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }
}
