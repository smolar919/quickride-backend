package com.quickride.demo.carrental.service;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.exceptions.ErrorCode;
import com.quickride.demo.carrental.model.Car;
import com.quickride.demo.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public Car getCarById(String id) throws ApplicationException {
        return carRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.CAR_NOT_FOUND));
    }
}
