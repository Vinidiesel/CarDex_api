package com.api.cars.controllers;

import com.api.cars.dtos.CarDto;
import com.api.cars.models.Car;
import com.api.cars.models.Images;
import com.api.cars.services.CarService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/car")
public class CarController {

    private static final String linkImages = "/CarDex/cars/carImages/";

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
        return new ResponseEntity<>(carPage, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Object> saveCar(@RequestPart("car") @Valid CarDto carDto){
        if(carService.existsByModelCarAndCarCategory(carDto.getModelCar(),carDto.getCarCategory())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This car already exists: " + carDto.getModelCar());
        }
        var car = new Car();
        BeanUtils.copyProperties(carDto, car);

        return ResponseEntity.status(HttpStatus.CREATED).body(carService.save(car));
    }

    public Set<Images> uploadImage(MultipartFile[] multipartFiles, UUID id) throws IOException {
        Set<Images> images = new HashSet<>();

        for (MultipartFile file: multipartFiles){

            byte[] bytes = file.getBytes();
            Path link = Paths.get(linkImages+String.valueOf(id)+String.valueOf(Math.random())+file.getOriginalFilename());
            Files.write(link, bytes);

            Images image = new Images();
            image.setName(String.valueOf(id)+String.valueOf(Math.random())+file.getOriginalFilename());

            images.add(image);
        }
        return images;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getCarById(@PathVariable(value = "id") UUID id){
        if (findById(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        findById(id).get().add(linkTo(methodOn(CarController.class).getAllCar(null)).withRel("All cars"));
        return new ResponseEntity<>(findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<Object> getCarByModel(@PathVariable(value = "model") String model){
        if (findByModel(model).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        findByModel(model).get().add(linkTo(methodOn(CarController.class).getAllCar(null)).withRel("All cars"));
        return new ResponseEntity<>(findByModel(model).get(), HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteCarById(@PathVariable(value = "id") UUID id){
        if (findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car nor found!");
        }
        carService.delete(findById(id).get());
        return ResponseEntity.status(HttpStatus.OK).body("Car is deleted successfully!");
    }

    @DeleteMapping("/model/{model}")
    public ResponseEntity<Object> deleteCarByModel(@PathVariable(value = "model") String model){
        if (findByModel(model).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car nor found!");
        }
        carService.delete(findByModel(model).get());
        return ResponseEntity.status(HttpStatus.OK).body("Car is deleted successfully!");
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateCarById(@PathVariable(value = "id") UUID id, @RequestBody @Valid CarDto carDto){
        return getObjectResponseEntity(carDto, findById(id));
    }

    @PutMapping("/model/{model}")
    public ResponseEntity<Object> updateCarByModel(@PathVariable(value = "model") String model, @RequestBody @Valid CarDto carDto){
        return getObjectResponseEntity(carDto, findByModel(model));
    }

    @PutMapping("image/{id}")
    public ResponseEntity<Object> updateCarImageById(@PathVariable(value = "id") UUID id,
                                                     @RequestPart("imageFile") MultipartFile[] files) throws IOException{
        return getObjectResponseEntity2(findById(id), files);
    }

    @GetMapping("image/{id}")
    public byte[] getImage(@PathVariable(value = "id") UUID id) throws IOException {
        Set<Images> images = findById(id).get().getCarImages();
        if (!images.isEmpty()) {
            for (Images image : images) {
                System.out.println(linkImages + image.getName());
                File file = new File(linkImages + image.getName());
                return Files.readAllBytes(file.toPath());
            }
        }
        return null;
    }

    private ResponseEntity<Object> getObjectResponseEntity2(Optional<Car> carOptional,
                                                            MultipartFile[] files) throws IOException {
        if (carOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!");
        }
        var car = new Car();
        BeanUtils.copyProperties(carOptional.get(), car);
        car.setId(carOptional.get().getId());
        Set<Images> images = uploadImage(files, car.getId());
        car.setCarImages(images);
        return ResponseEntity.status(HttpStatus.OK).body(carService.save(car));
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

    private Optional<Car> findById(UUID id){
        return carService.findById(id);
    }

    private Optional<Car> findByModel(String model){
        return carService.findByModel(model);
    }

}

