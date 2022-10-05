package com.api.cars.dtos;

import com.api.cars.models.Manufacturer;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class EngineDto {

    @NotBlank
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String cylinders;
    @NotBlank
    private Integer maximumEngineSpeed;
    @NotBlank
    private String engineSuction;
    @NotBlank
    private Integer maximumEnginePower;
    @NotBlank
    private Double engineLiter;
    @NotBlank
    private Manufacturer manufacturer;

}
