package com.api.cars.repositories;

import com.api.cars.models.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImagesRepository extends JpaRepository<Images, UUID> {
}
