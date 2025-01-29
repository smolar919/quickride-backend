package com.quickride.demo.carrental.service;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.exceptions.ErrorCode;
import com.quickride.demo.carrental.forms.AddCarForm;
import com.quickride.demo.carrental.forms.EditCarForm;
import com.quickride.demo.carrental.model.Car;
import com.quickride.demo.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAvailableCars() {
        return carRepository.findByAvailableTrue();
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car addCar(AddCarForm addCarForm) {
        Car car = Car.builder()
                .make(addCarForm.getMake())
                .model(addCarForm.getModel())
                .year(addCarForm.getYear())
                .available(true)
                .category(addCarForm.getCategory())
                .id(UUID.randomUUID().toString())
                .pricePerDay(addCarForm.getPricePerDay())
                .build();
        return carRepository.save(car);
    }

    public Car getCarById(String id) throws ApplicationException {
        return carRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.CAR_NOT_FOUND));
    }

    public Car editCar(String id, EditCarForm editCarForm) {
        Car car = carRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.CAR_NOT_FOUND));
        car.setMake(editCarForm.getMake());
        car.setYear(editCarForm.getYear());
        car.setPricePerDay(editCarForm.getPricePerDay());
        car.setCategory(editCarForm.getCategory());
        return carRepository.save(car);
    }

    public void deleteCar(String id) {
        carRepository.deleteById(id);
    }
}
