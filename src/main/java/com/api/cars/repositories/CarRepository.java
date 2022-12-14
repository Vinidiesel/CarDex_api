package com.api.cars.repositories;

import com.api.cars.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    @Override
    boolean existsById(UUID id);
    boolean existsByModelCarAndCarCategory(String modelCar, Integer carCategory);

    Optional<Car> findCarByModelCar(String modelCar);

}
