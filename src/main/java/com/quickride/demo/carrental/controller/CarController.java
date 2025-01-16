package com.quickride.demo.carrental.controller;

import com.quickride.demo.carrental.exceptions.ApplicationException;
import com.quickride.demo.carrental.model.Car;
import com.quickride.demo.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/available")
    public List<Car> getAvailableCars() {
        return carService.getAvailableCars();
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable String id) throws ApplicationException {
        return carService.getCarById(id);
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }
}