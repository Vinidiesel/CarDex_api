package com.api.cars.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class ManufacturerDto {

    @NotBlank
    private String name;
    @NotNull
    @Past
    private LocalDate foundation;
    @NotBlank
    private String headOffice;
    @NotBlank
    private String founderName;

}
