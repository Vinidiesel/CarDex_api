package com.api.cars.dtos;

import com.api.cars.models.Car;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class TransmissionDto {

    @NotBlank
    private UUID id;
    @NotBlank
    private String exchange;
    @NotBlank
    private Integer traction;
    @NotBlank
    private Car car;

}
