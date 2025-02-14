package com.quickride.demo.carrental.service;

import com.quickride.demo.carrental.dto.ReservationPeriodDTO;
import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.exceptions.ErrorCode;
import com.quickride.demo.carrental.forms.CreateReservationForm;
import com.quickride.demo.carrental.forms.EditReservationForm;
import com.quickride.demo.carrental.model.AppUser;
import com.quickride.demo.carrental.model.Car;
import com.quickride.demo.carrental.model.Reservation;
import com.quickride.demo.carrental.repository.CarRepository;
import com.quickride.demo.carrental.repository.ReservationRepository;
import com.quickride.demo.carrental.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    public Reservation createReservation(@Valid CreateReservationForm reservationForm) {

        AppUser appUser = userRepository.findById(reservationForm.getAppUserId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Car car = carRepository.findById(reservationForm.getCarId()).orElseThrow(() -> new ApplicationException(ErrorCode.CAR_NOT_FOUND));

        List<Reservation> existingReservations = reservationRepository.findOverlappingReservations(
                car.getId(), reservationForm.getStartDate(), reservationForm.getEndDate());

        if (!existingReservations.isEmpty()) {
            throw new ApplicationException(ErrorCode.CAR_ALREADY_BOOKED);
        }

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .appUser(appUser)
                .car(car)
                .endDate(reservationForm.getEndDate())
                .startDate(reservationForm.getStartDate())
                .confirmed(false)
                .fullPrice(reservationForm.getFullPrice())
                .build();
        return reservationRepository.save(reservation);
    }

    public List<ReservationPeriodDTO> getReservedDatesForCar(String carId) {
        List<Reservation> reservations = reservationRepository.findReservationsByCarId(carId);

        return reservations.stream()
                .map(reservation -> new ReservationPeriodDTO(
                        reservation.getStartDate(),
                        reservation.getEndDate()
                ))
                .collect(Collectors.toList());
    }

    public List<Reservation> getReservationsByAppUserId(String userId) {
        return reservationRepository.findByAppUserId(userId);
    }

    public Reservation getReservationById(String id) throws ApplicationException {
        return reservationRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public Reservation editReservation(String id, @Valid EditReservationForm form) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.setStartDate(form.getStartDate());
        reservation.setEndDate(form.getEndDate());
        reservation.setConfirmed(form.isConfirmed());
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
