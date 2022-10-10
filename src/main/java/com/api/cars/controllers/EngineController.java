package com.api.cars.controllers;

import com.api.cars.dtos.EngineDto;
import com.api.cars.models.Engine;
import com.api.cars.services.EngineService;
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
@RequestMapping("/engine")
public class EngineController {

    final EngineService engineService;

    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    @GetMapping
    public ResponseEntity<Page<Engine>> getAllEngines(@PageableDefault Pageable pageable){
        Page<Engine> enginePage = engineService.findAll(pageable);
        if (enginePage.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Engine engine : enginePage){
            String name = engine.getName();
            engine.add(linkTo(methodOn(EngineController.class).getEngineByName(name)).withSelfRel());
        }
        return new ResponseEntity<>(enginePage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> saveEngine(@RequestBody @Valid EngineDto engineDto){
        if (engineService.existsByNameAndCylinders(engineDto.getName(), engineDto.getCylinders())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This is engine already exists: " + engineDto.getName());
        }
        var engine = new Engine();
        BeanUtils.copyProperties(engineDto, engine);
        return ResponseEntity.status(HttpStatus.CREATED).body(engineService.save(engine));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getEngineById(@PathVariable(value = "id")UUID id){
        if (findById(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        findById(id).get().add(linkTo(methodOn(EngineController.class).getAllEngines(null)).withRel("All engines"));
        return new ResponseEntity<>(findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Object> getEngineByName(@PathVariable(value = "name") String name){
        if (findByName(name).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Pageable pageable = null;
        findByName(name).get().add(linkTo(methodOn(EngineController.class).getAllEngines(null)).withRel("All engines"));
        return new ResponseEntity<>(findByName(name).get(), HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteEngineById(@PathVariable(value = "id") UUID id){
        if (findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Engine not found");
        }
        engineService.delete(findById(id).get());
        return ResponseEntity.status(HttpStatus.OK).body("Car is deleted successfully");
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<Object> deleteEngineByName(@PathVariable(value = "name") String name){
        if (findByName(name).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Engine not found");
        }
        engineService.delete(findByName(name).get());
        return ResponseEntity.status(HttpStatus.OK).body("Car is deleted successfully");
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateEngineById(@PathVariable(value = "id") UUID id, @RequestBody @Valid EngineDto engineDto){
        return getObjectResponseEntity(engineDto, findById(id));
    }

    @PutMapping("/name/{name}")
    public ResponseEntity<Object> updateEngineByName(@PathVariable(value = "name") String name, @RequestBody @Valid EngineDto engineDto){
        return getObjectResponseEntity(engineDto, findByName(name));
    }

    private ResponseEntity<Object> getObjectResponseEntity(@RequestBody @Valid EngineDto engineDto, Optional<Engine> engineOptional) {
        if (engineOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!");
        }
        var engine = new Engine();
        BeanUtils.copyProperties(engineDto, engine);
        engine.setId(engineOptional.get().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(engineService.save(engine));
    }

    private Optional<Engine> findById(UUID id){
        return engineService.findById(id);
    }
    private Optional<Engine> findByName(String name){
        return engineService.findByName(name);
    }
}
