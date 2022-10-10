package com.api.cars.controllers;

import com.api.cars.dtos.ManufacturerDto;
import com.api.cars.models.Manufacturer;
import com.api.cars.services.ManufacturerService;
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
@RequestMapping("/manufacturer")
public class ManufacturerController {

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
    public ResponseEntity<Object> saveManufacturers(@RequestBody @Valid ManufacturerDto manufacturerDto){
        if (manufacturerService.existsByNameAndFounderName(manufacturerDto.getName(), manufacturerDto.getFounder())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This Manufacturer already exits: " + manufacturerDto.getName() + "Founder: " + manufacturerDto.getFounder());
        }
        var manufacturer = new Manufacturer();
        BeanUtils.copyProperties(manufacturerDto, manufacturer);
        return ResponseEntity.status(HttpStatus.CREATED).body(manufacturerService.save(manufacturer));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getManufacturerById(@PathVariable(value = "id") UUID id){
        if (findById(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        findById(id).get().add(linkTo(methodOn(ManufacturerController.class).getAllManufacturers(null)).withRel("All manufacturers"));
        return new ResponseEntity<>(findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/name/founder/{name}/{founder}")
    public ResponseEntity<Object> getManufacturerByNameAndFounder(@PathVariable(value = "name") String name, @PathVariable(value = "founder") String founder){
        if (findByNameAndFounder(name, founder).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        findByNameAndFounder(name, founder).get().add(linkTo(methodOn(ManufacturerController.class).getAllManufacturers(null)).withRel("All manufacturers"));
        return new ResponseEntity<>(findByNameAndFounder(name, founder).get(), HttpStatus.OK);
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

    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateManufacturerById(@PathVariable(value = "id") UUID id, @RequestBody @Valid ManufacturerDto manufacturerDto){
        return getObjectResponseEntity(manufacturerDto, findById(id));
    }

    @PutMapping("/name/founder/{name}/{founder}")
    public ResponseEntity<Object> updateManufacturerByNameAndFounder(@PathVariable(value = "name") String name, @PathVariable(value = "founder") String founder, @RequestBody @Valid ManufacturerDto manufacturerDto){
        return getObjectResponseEntity(manufacturerDto, findByNameAndFounder(name, founder));
    }

    private ResponseEntity<Object> getObjectResponseEntity(@RequestBody @Valid ManufacturerDto manufacturerDto, Optional<Manufacturer> manufacturerOptional) {
        if (manufacturerOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!");
        }
        var manufacturer = new Manufacturer();
        BeanUtils.copyProperties(manufacturerDto, manufacturer);
        manufacturer.setId(manufacturerOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(manufacturerService.save(manufacturer));
    }

    private Optional<Manufacturer> findById(UUID id){
        return manufacturerService.findById(id);
    }

    private Optional<Manufacturer> findByNameAndFounder(String name, String founder){
        return manufacturerService.findByNameAndFounderName(name, founder);
    }

}
