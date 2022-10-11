package com.api.cars.models;

import com.api.cars.models.enums.CarCategory;
import com.api.cars.models.enums.EnginePosition;
import com.api.cars.models.enums.Traction;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "db_cars")
public class Car extends RepresentationModel<Car> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true, length = 40)
    private String modelCar;
    @Column(nullable = false, length = 5)
    private Double maxSpeed;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startOfProduction;
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endOfProduction;
    @Column(nullable = false, length = 5)
    private Double zeroToAHundred;
    @Column(nullable = false, length = 12)
    private Integer unitsProduced;
    @Column
    private String carImage;
    @Column(nullable = false, length = 25)
    private String exchange;

    @Column(nullable = false, length = 20)
    private Integer traction;
    @Column(nullable = false, length = 20)
    private Integer carCategory;
    @Column(nullable = false, length = 20)
    private Integer enginePosition;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;
    @ManyToOne(optional = false)
    @JoinColumn(name = "engine_id")
    private Engine engine;
    @OneToMany(mappedBy = "car", cascade = CascadeType.REMOVE)
    private final Set<Images> images = new HashSet<>();


    public Car(UUID id, String modelCar, Double maxSpeed, LocalDate startOfProduction, LocalDate endOfProduction, Double zeroToAHundred, Integer unitsProduced, String carImage, String exchange, Traction traction, CarCategory carCategory, EnginePosition enginePosition, Manufacturer manufacturer, Engine engine) {
        this.id = id;
        this.modelCar = modelCar;
        this.maxSpeed = maxSpeed;
        this.startOfProduction = startOfProduction;
        this.endOfProduction = endOfProduction;
        this.zeroToAHundred = zeroToAHundred;
        this.unitsProduced = unitsProduced;
        this.carImage = carImage;
        this.exchange = exchange;
        setTraction(traction);
        setCarCategory(carCategory);
        setEnginePosition(enginePosition);
        this.manufacturer = manufacturer;
        this.engine = engine;
    }

    public Car() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getModelCar() {
        return modelCar;
    }

    public void setModelCar(String modelCar) {
        this.modelCar = modelCar;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public LocalDate getStartOfProduction() {
        return startOfProduction;
    }

    public void setStartOfProduction(LocalDate startOfProduction) {
        this.startOfProduction = startOfProduction;
    }

    public LocalDate getEndOfProduction() {
        return endOfProduction;
    }

    public void setEndOfProduction(LocalDate endOfProduction) {
        this.endOfProduction = endOfProduction;
    }

    public Double getZeroToAHundred() {
        return zeroToAHundred;
    }

    public void setZeroToAHundred(Double zeroToAHundred) {
        this.zeroToAHundred = zeroToAHundred;
    }

    public Integer getUnitsProduced() {
        return unitsProduced;
    }

    public void setUnitsProduced(Integer unitsProduced) {
        this.unitsProduced = unitsProduced;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Traction getTraction() {
        return Traction.valueOf(traction);
    }

    public void setTraction(Traction traction) {
        this.traction = traction.getCode();
    }

    public CarCategory getCarCategory() {
        return CarCategory.valueOf(carCategory);
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory.getCode();
    }

    public EnginePosition getEnginePosition() {
        return EnginePosition.valueOf(enginePosition);
    }

    public void setEnginePosition(EnginePosition enginePosition) {
        this.enginePosition = enginePosition.getCode();
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    //produção,peso,aceleração,unidades produzidas
    //mais classes: CarroceriaEnums, Classeenums(exemplo carros esportivos), Layout(exemplo motor traseiro e tração dianteira)

}
