package com.api.cars.services;

import com.api.cars.models.Manufacturer;
import com.api.cars.repositories.ManufacturerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManufacturerService {

    final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public boolean existsById(UUID uuid) {
        return manufacturerRepository.existsById(uuid);
    }

    public boolean existsByNameAndFounderName(String name, String founderName) {
        return manufacturerRepository.existsByNameAndFounderName(name, founderName);
    }

    @Transactional
    public Manufacturer save(Manufacturer manufacturer){
        return manufacturerRepository.save(manufacturer);
    }

    public Page<Manufacturer> findAll(Pageable pageable){
        return manufacturerRepository.findAll(pageable);
    }

    public Optional<Manufacturer> findById(UUID id){
        return manufacturerRepository.findById(id);
    }

    @Transactional
    public void delete(Manufacturer manufacturer){
            manufacturerRepository.delete(manufacturer);
    }

    public Optional<Manufacturer> findByNameAndFounderName(String name, String founder) {
        return manufacturerRepository.findByNameAndFounderName(name, founder);
    }
}
