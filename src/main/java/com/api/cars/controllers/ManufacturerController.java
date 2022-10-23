package com.api.cars.controllers;

import com.api.cars.dtos.ManufacturerDto;
import com.api.cars.models.Images;
import com.api.cars.models.Manufacturer;
import com.api.cars.services.ManufacturerService;
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
@RequestMapping("/manufacturer")
public class ManufacturerController {

    private static final String linkImages = "/CarDex/cars/manufacturerImages/";

    final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public ResponseEntity<Page<Manufacturer>> getAllManufacturers(@PageableDefault Pageable pageable){
        Page<Manufacturer> manufacturerPage = manufacturerService.findAll(pageable);
        if (manufacturerPage.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Manufacturer manufacturer : manufacturerPage){
            String name = manufacturer.getName();
            String founder = manufacturer.getFounderName();
            manufacturer.add(linkTo(methodOn(ManufacturerController.class).getManufacturerByNameAndFounder(name, founder)).withSelfRel());
        }
        return new ResponseEntity<>(manufacturerPage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> saveManufacturers(@RequestBody @Valid ManufacturerDto manufacturerDto) {
        if (manufacturerService.existsByNameAndFounderName(manufacturerDto.getName(), manufacturerDto.getFounderName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This Manufacturer already exits: " + manufacturerDto.getName() + "Founder: " + manufacturerDto.getFounderName());
        }
        var manufacturer = new Manufacturer();
        BeanUtils.copyProperties(manufacturerDto, manufacturer);

        return ResponseEntity.status(HttpStatus.CREATED).body(manufacturerService.save(manufacturer));
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
    public ResponseEntity<Object> getManufacturerById(@PathVariable(value = "id") UUID id){
        return findById(id).<ResponseEntity<Object>>map(manufacturer -> ResponseEntity.status(HttpStatus.OK).body(manufacturer)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manufacturer not found!"));
    }

    @GetMapping("/name/founder/{name}/{founder}")
    public ResponseEntity<Object> getManufacturerByNameAndFounder(@PathVariable(value = "name") String name, @PathVariable(value = "founder") String founder){
        return findByNameAndFounder(name, founder).<ResponseEntity<Object>>map(manufacturer -> ResponseEntity.status(HttpStatus.OK).body(manufacturer)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manufacturer not found!"));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteManufacturerById(@PathVariable(value = "id") UUID id){
        if (findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manufacturer not found!");
        }
        manufacturerService.delete(findById(id).get());
        return ResponseEntity.status(HttpStatus.OK).body("Manufacturer is deleted successfully!");
    }

    @DeleteMapping("/name/founder/{name}/{founder}")
    public ResponseEntity<Object> deleteManufacturerByNameAndFounder(@PathVariable(value = "name") String name, @PathVariable(value = "founder") String founder){
        if (findByNameAndFounder(name, founder).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manufacturer not found!");
        }
        manufacturerService.delete(findByNameAndFounder(name, founder).get());
        return ResponseEntity.status(HttpStatus.OK).body("Manufacturer is deleted successfully!");
    }

    @PutMapping("id/{id}")
    public ResponseEntity<Object> updateManufacturerById(@PathVariable(value = "id") UUID id,
                                                         @RequestBody @Valid ManufacturerDto manufacturerDto) {
        return getObjectResponseEntity(manufacturerDto, findById(id));
    }



    @PutMapping("/name/founder/{name}/{founder}")
    public ResponseEntity<Object> updateManufacturerByNameAndFounder(@PathVariable(value = "name") String name,
                                                                     @PathVariable(value = "founder") String founder,
                                                                     @RequestBody @Valid ManufacturerDto manufacturerDto) {
        return getObjectResponseEntity(manufacturerDto, findByNameAndFounder(name, founder));
    }

    private ResponseEntity<Object> getObjectResponseEntity(ManufacturerDto manufacturerDto, Optional<Manufacturer> manufacturerOptional) {
        if (manufacturerOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manufacturer not found!");
        }
        var manufacturer = new Manufacturer();
        BeanUtils.copyProperties(manufacturerDto, manufacturer);
        manufacturer.setId(manufacturerOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(manufacturerService.save(manufacturer));
    }


    @PutMapping("image/{id}")
    public ResponseEntity<Object> updateManufacturerImageById(@PathVariable(value = "id") UUID id,
                                                              @RequestPart("imageFile") MultipartFile[] files) throws IOException{
        return getObjectResponseEntity2(findById(id), files);
    }

    @GetMapping("image/{id}")
    public byte[] getImage(@PathVariable(value = "id") UUID id) throws IOException {
        Set<Images> images = findById(id).get().getManufacturerImages();
        if (!images.isEmpty()) {
            for (Images image : images) {
                System.out.println(linkImages + image.getName());
                File file = new File(linkImages + image.getName());
                return Files.readAllBytes(file.toPath());
            }
        }
        return null;
    }

    private ResponseEntity<Object> getObjectResponseEntity2(Optional<Manufacturer> manufacturerOptional,
                                                           MultipartFile[] files) throws IOException {
        if (manufacturerOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!");
        }
        var manufacturer = new Manufacturer();
        BeanUtils.copyProperties(manufacturerOptional.get(), manufacturer);
        Set<Images> images = uploadImage(files, manufacturer.getId());
        manufacturer.setManufacturerImages(images);
        return ResponseEntity.status(HttpStatus.OK).body(manufacturerService.save(manufacturer));
    }

    private Optional<Manufacturer> findById(UUID id){
        return manufacturerService.findById(id);
    }

    private Optional<Manufacturer> findByNameAndFounder(String name, String founder){
        return manufacturerService.findByNameAndFounderName(name, founder);
    }

}
