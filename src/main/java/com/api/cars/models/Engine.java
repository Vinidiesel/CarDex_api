package com.api.cars.models;

import com.api.cars.models.enums.Fuel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_engine")
public class Engine extends RepresentationModel<Engine> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true, length = 45)
    private String name;
    @Column(nullable = false, length = 45)
    private String cylinders;
    @Column(nullable = false, length = 6)
    private Integer maximumEngineSpeed;
    @Column(nullable = false, length = 20)
    private String engineSuction;
    @Column(nullable = false, length = 6)
    private Integer maximumEnginePower;
    @Column(nullable = false, length = 5)
    private Double engineLiter;

    @Column(nullable = false, length = 10)
    private Integer fuel;

    @JsonIgnore
    @OneToMany(mappedBy = "engine")
    private final Set<Car> car = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    public Engine(UUID id, String name, String cylinders, Integer maximumEngineSpeed, String engineSuction, Integer maximumEnginePower, Double engineLiter, Fuel fuel, Manufacturer manufacturer) {
        this.id = id;
        this.name = name;
        this.cylinders = cylinders;
        this.maximumEngineSpeed = maximumEngineSpeed;
        this.engineSuction = engineSuction;
        this.maximumEnginePower = maximumEnginePower;
        this.engineLiter = engineLiter;
        setFuel(fuel);
        this.manufacturer = manufacturer;
    }

    public Engine() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCylinders() {
        return cylinders;
    }

    public void setCylinders(String cylinders) {
        this.cylinders = cylinders;
    }

    public Integer getMaximumEngineSpeed() {
        return maximumEngineSpeed;
    }

    public void setMaximumEngineSpeed(Integer maximumEngineSpeed) {
        this.maximumEngineSpeed = maximumEngineSpeed;
    }

    public String getEngineSuction() {
        return engineSuction;
    }

    public void setEngineSuction(String engineSuction) {
        this.engineSuction = engineSuction;
    }

    public Integer getMaximumEnginePower() {
        return maximumEnginePower;
    }

    public void setMaximumEnginePower(Integer maximumEnginePower) {
        this.maximumEnginePower = maximumEnginePower;
    }

    public Double getEngineLiter() {
        return engineLiter;
    }

    public void setEngineLiter(Double engineLiter) {
        this.engineLiter = engineLiter;
    }

    public Fuel getFuel() {
        return Fuel.valueOf(fuel);
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel.getCode();
    }

    public Set<Car> getCar() {
        return car;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
}
