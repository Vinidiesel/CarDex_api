package com.api.cars.repositories;

import com.api.cars.models.Car;
import com.api.cars.models.Engine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EngineRepository extends JpaRepository<Engine, UUID> {

    @Override
    boolean existsById(UUID uuid);
    boolean existsByNameAndCylinders(String name, String cylinders);

    Optional<Engine> findByName(String name);
}
