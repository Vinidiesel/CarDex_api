package com.api.cars.repositories;

import com.api.cars.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, UUID> {

    @Override
    boolean existsById(UUID uuid);
    boolean existsByNameAndFounderName(String name, String founderName);

    Optional<Manufacturer> findByNameAndFounderName(String name, String founderName);

}
