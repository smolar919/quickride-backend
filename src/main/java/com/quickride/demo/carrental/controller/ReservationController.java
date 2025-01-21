package com.quickride.demo.carrental.controller;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.forms.CreateReservationForm;
import com.quickride.demo.carrental.forms.EditReservationForm;
import com.quickride.demo.carrental.model.Reservation;
import com.quickride.demo.carrental.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public Reservation createReservation(@RequestBody CreateReservationForm reservation) {
        return reservationService.createReservation(reservation);
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getUserReservations(@PathVariable String userId) {
        return reservationService.getReservationsByAppUserId(userId);
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable String id) throws ApplicationException {
        return reservationService.getReservationById(id);
    }

    @PutMapping("/{id}")
    public Reservation editReservation(@PathVariable String id, @RequestBody EditReservationForm form) throws ApplicationException {
        return reservationService.editReservation(id, form);
    }
}