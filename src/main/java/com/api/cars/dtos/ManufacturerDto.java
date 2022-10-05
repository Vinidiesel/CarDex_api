package com.api.cars.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

@Data
public class ManufacturerDto {

    @NotBlank
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private Date foundation;
    @NotBlank
    private String headOffice;
    @NotBlank
    private String founder;

}
