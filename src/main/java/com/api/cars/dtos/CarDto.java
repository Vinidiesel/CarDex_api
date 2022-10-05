package com.api.cars.dtos;

import com.api.cars.models.Engine;
import com.api.cars.models.Manufacturer;
import com.api.cars.models.Transmission;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

@Data
public class CarDto {

    @NotBlank
    private UUID id;
    @NotBlank
    private String modelCar;
    @NotBlank
    private Double maxSpeed;
    @NotBlank
    private Date startOfProduction;
    @NotBlank
    private Date endOfProduction;
    @NotBlank
    private Double zeroToAHundred;
    @NotBlank
    private Integer unitsProduced;

    @NotBlank
    private Integer carCategory;
    @NotBlank
    private Integer enginePosition;

    @NotBlank
    private Transmission transmission;
    @NotBlank
    private Manufacturer manufacturer;
    @NotBlank
    private Engine engine;


}
