package com.quickride.demo;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.forms.AddCarForm;
import com.quickride.demo.carrental.forms.EditCarForm;
import com.quickride.demo.carrental.model.Car;
import com.quickride.demo.carrental.model.CarCategory;
import com.quickride.demo.carrental.repository.CarRepository;
import com.quickride.demo.carrental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableCars() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2022, BigDecimal.valueOf(50), CarCategory.ECONOMY, true);
        Car car2 = new Car("2", "Honda", "Civic", 2021, BigDecimal.valueOf(55), CarCategory.ECONOMY, true);
        when(carRepository.findByAvailableTrue()).thenReturn(List.of(car1, car2));

        List<Car> availableCars = carService.getAvailableCars();

        assertEquals(2, availableCars.size());
        assertTrue(availableCars.contains(car1));
        assertTrue(availableCars.contains(car2));
    }

    @Test
    void testGetAllCars() {
        Car car1 = new Car("1", "Toyota", "Corolla", 2022, BigDecimal.valueOf(50), CarCategory.ECONOMY, true);
        Car car2 = new Car("2", "Honda", "Civic", 2021, BigDecimal.valueOf(55), CarCategory.ECONOMY, true);
        when(carRepository.findAll()).thenReturn(List.of(car1, car2));

        List<Car> allCars = carService.getAllCars();

        assertEquals(2, allCars.size());
        assertTrue(allCars.contains(car1));
        assertTrue(allCars.contains(car2));
    }

    @Test
    void testAddCar() {
        AddCarForm addCarForm = new AddCarForm();
        addCarForm.setMake("Ford");
        addCarForm.setModel("Focus");
        addCarForm.setYear(2020);
        addCarForm.setCategory(CarCategory.COMPACT);
        addCarForm.setPricePerDay(BigDecimal.valueOf(45));

        Car car = Car.builder()
                .id(UUID.randomUUID().toString())
                .make(addCarForm.getMake())
                .model(addCarForm.getModel())
                .year(addCarForm.getYear())
                .available(true)
                .category(addCarForm.getCategory())
                .pricePerDay(addCarForm.getPricePerDay())
                .build();

        when(carRepository.save(any(Car.class))).thenReturn(car);

        // When
        Car addedCar = carService.addCar(addCarForm);

        // Then
        assertNotNull(addedCar);
        assertEquals("Ford", addedCar.getMake());
        assertEquals("Focus", addedCar.getModel());
        assertEquals(2020, addedCar.getYear());
        assertEquals(BigDecimal.valueOf(45), addedCar.getPricePerDay());
        assertEquals(CarCategory.COMPACT, addedCar.getCategory());
        assertTrue(addedCar.isAvailable());
    }

    @Test
    void testGetCarById_CarExists() throws ApplicationException {
        Car car = new Car("1", "Toyota", "Corolla", 2022, BigDecimal.valueOf(50), CarCategory.ECONOMY, true);
        when(carRepository.findById("1")).thenReturn(Optional.of(car));

        Car foundCar = carService.getCarById("1");

        assertNotNull(foundCar);
        assertEquals("Toyota", foundCar.getMake());
    }

    @Test
    void testGetCarById_CarNotFound() {
        when(carRepository.findById("999")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> carService.getCarById("999"));
        assertEquals("Car not found", exception.getMessage());
    }

    @Test
    void testEditCar_CarExists() {
        Car existingCar = new Car("1", "Toyota", "Corolla", 2022, BigDecimal.valueOf(50), CarCategory.LUXURY, true);
        EditCarForm editCarForm = new EditCarForm();
        editCarForm.setMake("Honda");
        editCarForm.setYear(2023);
        editCarForm.setPricePerDay(BigDecimal.valueOf(60));
        editCarForm.setCategory(CarCategory.SUV);

        when(carRepository.findById("1")).thenReturn(Optional.of(existingCar));
        when(carRepository.save(any(Car.class))).thenReturn(existingCar);

        Car updatedCar = carService.editCar("1", editCarForm);

        assertNotNull(updatedCar);
        assertEquals("Honda", updatedCar.getMake());
        assertEquals(2023, updatedCar.getYear());
        assertEquals(BigDecimal.valueOf(60), updatedCar.getPricePerDay());
        assertEquals(CarCategory.SUV, updatedCar.getCategory());
    }

    @Test
    void testEditCar_CarNotFound() {
        EditCarForm editCarForm = new EditCarForm();
        editCarForm.setMake("Honda");
        when(carRepository.findById("999")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> carService.editCar("999", editCarForm));
        assertEquals("Car not found", exception.getMessage());
    }

    @Test
    void testDeleteCar() {
        String carId = "1";

        carService.deleteCar(carId);

        verify(carRepository, times(1)).deleteById(carId);
    }
}
