package com.api.cars.controllers;

import com.api.cars.dtos.EngineDto;
import com.api.cars.models.Car;
import com.api.cars.models.Engine;
import com.api.cars.models.Images;
import com.api.cars.services.EngineService;
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
@RequestMapping("/engine")
public class EngineController {

    private static final String linkImages = "/CarDex/cars/engineImages/";

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

    @PutMapping("image/{id}")
    public ResponseEntity<Object> updateEngineImageById(@PathVariable(value = "id") UUID id,
                                                        @RequestPart("imageFile") MultipartFile[] files) throws IOException{
        return getObjectResponseEntity2(findById(id), files);
    }

    @GetMapping("image/{id}")
    public byte[] getImage(@PathVariable(value = "id") UUID id) throws IOException {
        Set<Images> images = findById(id).get().getEngineImages();
        if (!images.isEmpty()) {
            for (Images image : images) {
                System.out.println(linkImages + image.getName());
                File file = new File(linkImages + image.getName());
                return Files.readAllBytes(file.toPath());
            }
        }
        return null;
    }

    private ResponseEntity<Object> getObjectResponseEntity2(Optional<Engine> engineOptional,
                                                            MultipartFile[] files) throws IOException {
        if (engineOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!");
        }
        var engine = new Engine();
        BeanUtils.copyProperties(engineOptional.get(), engine);
        engine.setId(engineOptional.get().getId());
        Set<Images> images = uploadImage(files, engine.getId());
        engine.setEngineImages(images);
        return ResponseEntity.status(HttpStatus.OK).body(engineService.save(engine));
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
