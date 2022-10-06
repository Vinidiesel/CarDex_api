package com.api.cars.services;

import com.api.cars.models.Car;
import com.api.cars.repositories.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService {

    final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public boolean existsById(UUID uuid) {
        return carRepository.existsById(uuid);
    }

    public boolean existsByModelCarAndCarCategory(String modelCar, Integer carCategory) {
        return carRepository.existsByModelCarAndCarCategory(modelCar, carCategory);
    }

    @Transactional
    public Car save(Car car) {
        return carRepository.save(car);
    }

    public Page<Car> findAll(Pageable pageable){
        return carRepository.findAll(pageable);
    }

    public Optional<Car> findById(UUID id){
        return carRepository.findById(id);
    }

    public Optional<Car> findByModel(String model){
        return carRepository.findCarByModelCar(model);
    }

    @Transactional
    public void delete(Car car){
        carRepository.delete(car);
    }

}
