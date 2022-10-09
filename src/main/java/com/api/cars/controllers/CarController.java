package com.api.cars.controllers;

import com.api.cars.dtos.CarDto;
import com.api.cars.models.Car;
import com.api.cars.models.Manufacturer;
import com.api.cars.services.CarService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/car")
public class CarController {

    final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<Page<Car>> getAllCar(@PageableDefault Pageable pageable){
        Page<Car> carPage = carService.findAll(pageable);
        if (carPage.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Car car : carPage){
            String model = car.getModelCar();
            car.add(linkTo(methodOn(CarController.class).getCarByModel(model)).withSelfRel());
        }
        return  new ResponseEntity<Page<Car>>(carPage, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Object> saveCar(@RequestBody @Valid CarDto carDto){
        if(carService.existsByModelCarAndCarCategory(carDto.getModelCar(),carDto.getCarCategory())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This car already exists: " + carDto.getModelCar());
        }
        var car = new Car();
        BeanUtils.copyProperties(carDto, car);
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.save(car));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getCarById(@PathVariable(value = "id") UUID id){
        Optional<Car> carOptional = carService.findById(id);
        return carOptional.<ResponseEntity<Object>>map(car -> ResponseEntity.status(HttpStatus.OK).body(car)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!"));
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<Object> getCarByModel(@PathVariable(value = "model") String model){
        Optional<Car> carOptional = carService.findByModel(model);
        return carOptional.<ResponseEntity<Object>>map(car -> ResponseEntity.status(HttpStatus.OK).body(car)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!"));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteCarById(@PathVariable(value = "id") UUID id){
        Optional<Car> carOptional = carService.findById(id);
        if (carOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car nor found!");
        }
        carService.delete(carOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Car is deleted successfully!");
    }

    @DeleteMapping("/model/{model}")
    public ResponseEntity<Object> deleteCarByModel(@PathVariable(value = "model") String model){
        Optional<Car> carOptional = carService.findByModel(model);
        if (carOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car nor found!");
        }
        carService.delete(carOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Car is deleted successfully!");
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateCarById(@PathVariable(value = "id") UUID id, @RequestBody @Valid CarDto carDto){
        Optional<Car> carOptional = carService.findById(id);
        return getObjectResponseEntity(carDto, carOptional);
    }

    @PutMapping("/model/{model}")
    public ResponseEntity<Object> updateCarByModel(@PathVariable(value = "model") String model, @RequestBody @Valid CarDto carDto){
        Optional<Car> carOptional = carService.findByModel(model);
        return getObjectResponseEntity(carDto, carOptional);
    }

    private ResponseEntity<Object> getObjectResponseEntity(@RequestBody @Valid CarDto carDto, Optional<Car> carOptional) {
        if (carOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!");
        }
        var car = new Car();
        BeanUtils.copyProperties(carDto, car);
        car.setId(carOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(carService.save(car));
    }

}

