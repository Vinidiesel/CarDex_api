package com.api.cars.repositories;

import com.api.cars.models.Transmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransmissionRepository extends JpaRepository<Transmission, UUID> {

    @Override
    boolean existsById(UUID uuid);

}
