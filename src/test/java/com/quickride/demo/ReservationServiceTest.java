package com.quickride.demo;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.forms.CreateReservationForm;
import com.quickride.demo.carrental.forms.EditReservationForm;
import com.quickride.demo.carrental.model.AppUser;
import com.quickride.demo.carrental.model.Car;
import com.quickride.demo.carrental.model.Reservation;
import com.quickride.demo.carrental.repository.CarRepository;
import com.quickride.demo.carrental.repository.ReservationRepository;
import com.quickride.demo.carrental.repository.UserRepository;
import com.quickride.demo.carrental.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReservation_Success() {
        AppUser user = new AppUser();
        user.setId("user123");

        Car car = new Car();
        car.setId("car123");

        CreateReservationForm form = new CreateReservationForm();
        form.setAppUserId("user123");
        form.setCarId("car123");
        form.setStartDate(LocalDate.now());
        form.setEndDate(LocalDate.now().plusDays(5));
        form.setFullPrice(BigDecimal.valueOf(250));

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .appUser(user)
                .car(car)
                .startDate(form.getStartDate())
                .endDate(form.getEndDate())
                .confirmed(false)
                .fullPrice(form.getFullPrice())
                .build();

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(carRepository.findById("car123")).thenReturn(Optional.of(car));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation createdReservation = reservationService.createReservation(form);

        assertNotNull(createdReservation);
        assertEquals(user, createdReservation.getAppUser());
        assertEquals(car, createdReservation.getCar());
        assertEquals(form.getStartDate(), createdReservation.getStartDate());
        assertEquals(form.getEndDate(), createdReservation.getEndDate());
        assertEquals(BigDecimal.valueOf(250), createdReservation.getFullPrice());
        assertFalse(createdReservation.isConfirmed());
    }

    @Test
    void testCreateReservation_CarAlreadyBooked() {
        AppUser user = new AppUser();
        user.setId("user123");

        Car car = new Car();
        car.setId("car123");

        CreateReservationForm form = new CreateReservationForm();
        form.setAppUserId("user123");
        form.setCarId("car123");
        form.setStartDate(LocalDate.of(2025, 2, 1));
        form.setEndDate(LocalDate.of(2025, 2, 10));
        form.setFullPrice(BigDecimal.valueOf(300));

        Reservation existingReservation = new Reservation();
        existingReservation.setCar(car);
        existingReservation.setStartDate(LocalDate.of(2025, 2, 5));
        existingReservation.setEndDate(LocalDate.of(2025, 2, 15));

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(carRepository.findById("car123")).thenReturn(Optional.of(car));
        when(reservationRepository.findOverlappingReservations("car123", form.getStartDate(), form.getEndDate()))
                .thenReturn(List.of(existingReservation));

        ApplicationException exception = assertThrows(ApplicationException.class, () -> reservationService.createReservation(form));
        assertEquals("This car is already booked for the selected dates.", exception.getMessage());
    }

    @Test
    void testCreateReservation_UserNotFound() {
        CreateReservationForm form = new CreateReservationForm();
        form.setAppUserId("user123");
        form.setCarId("car123");

        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> reservationService.createReservation(form));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testCreateReservation_CarNotFound() {
        AppUser user = new AppUser();
        user.setId("user123");

        CreateReservationForm form = new CreateReservationForm();
        form.setAppUserId("user123");
        form.setCarId("car123");

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(carRepository.findById("car123")).thenReturn(Optional.empty());

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class, () -> reservationService.createReservation(form));
        assertEquals("Car not found", exception.getMessage());
    }

    @Test
    void testGetReservationsByAppUserId() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        when(reservationRepository.findByAppUserId("user123")).thenReturn(List.of(reservation1, reservation2));

        List<Reservation> reservations = reservationService.getReservationsByAppUserId("user123");

        assertEquals(2, reservations.size());
        assertTrue(reservations.contains(reservation1));
        assertTrue(reservations.contains(reservation2));
    }

    @Test
    void testGetReservationById_Success() {
        Reservation reservation = new Reservation();
        reservation.setId("res123");

        when(reservationRepository.findById("res123")).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.getReservationById("res123");

        assertNotNull(foundReservation);
        assertEquals("res123", foundReservation.getId());
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById("res123")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> reservationService.getReservationById("res123"));
        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    void testEditReservation_Success() {
        Reservation existingReservation = new Reservation();
        existingReservation.setId("res123");
        existingReservation.setStartDate(LocalDate.now());
        existingReservation.setEndDate(LocalDate.now().plusDays(3));
        existingReservation.setConfirmed(false);

        EditReservationForm editForm = new EditReservationForm();
        editForm.setStartDate(LocalDate.now().plusDays(1));
        editForm.setEndDate(LocalDate.now().plusDays(4));
        editForm.setConfirmed(true);

        when(reservationRepository.findById("res123")).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(existingReservation);

        Reservation updatedReservation = reservationService.editReservation("res123", editForm);

        assertNotNull(updatedReservation);
        assertEquals(editForm.getStartDate(), updatedReservation.getStartDate());
        assertEquals(editForm.getEndDate(), updatedReservation.getEndDate());
        assertTrue(updatedReservation.isConfirmed());
    }

    @Test
    void testEditReservation_NotFound() {
        EditReservationForm editForm = new EditReservationForm();
        editForm.setStartDate(LocalDate.now());
        editForm.setEndDate(LocalDate.now().plusDays(5));

        when(reservationRepository.findById("res123")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> reservationService.editReservation("res123", editForm));
        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    void testGetAllReservations() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        when(reservationRepository.findAll()).thenReturn(List.of(reservation1, reservation2));

        List<Reservation> reservations = reservationService.getAllReservations();

        assertEquals(2, reservations.size());
        assertTrue(reservations.contains(reservation1));
        assertTrue(reservations.contains(reservation2));
    }
}
