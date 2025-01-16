package com.quickride.demo.carrental.repository;

import com.quickride.demo.carrental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, String> {
    List<Car> findByAvailableTrue();
}
