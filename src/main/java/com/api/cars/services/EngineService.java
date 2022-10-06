package com.api.cars.services;

import com.api.cars.models.Engine;
import com.api.cars.repositories.EngineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class EngineService {

    final EngineRepository engineRepository;

    public EngineService(EngineRepository engineRepository) {
        this.engineRepository = engineRepository;
    }

    public boolean existsById(UUID uuid) {
        return engineRepository.existsById(uuid);
    }

    public boolean existsByNameAndCylinders(String name, String cylinders) {
        return engineRepository.existsByNameAndCylinders(name, cylinders);
    }

    @Transactional
    public Engine save(Engine engine){
        return engineRepository.save(engine);
    }

    public Page<Engine> findAll(Pageable pageable){
        return engineRepository.findAll(pageable);
    }

    public Optional<Engine> findById(UUID id){
        return engineRepository.findById(id);
    }

    public Optional<Engine> findByName(String name){
        return engineRepository.findByName(name);
    }

    @Transactional
    public void delete(Engine engine){
        engineRepository.delete(engine);
    }
}
