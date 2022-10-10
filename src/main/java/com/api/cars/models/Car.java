package com.api.cars.models;

import com.api.cars.models.enums.CarCategory;
import com.api.cars.models.enums.EnginePosition;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "db_cars")
public class Car extends RepresentationModel<Car> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true, length = 10)
    private String modelCar;
    @Column(nullable = false, length = 5)
    private Double maxSpeed;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startOfProduction;
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endOfProduction;
    @Column(nullable = false, length = 5)
    private Double zeroToAHundred;
    @Column(nullable = false, length = 12)
    private Integer unitsProduced;
    @Column
    private String carImage;

    @Column(nullable = false, length = 20)
    private Integer carCategory;
    @Column(nullable = false, length = 20)
    private Integer enginePosition;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Transmission transmission;
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public Car(UUID id, String modelCar, Double maxSpeed, Date startOfProduction, Date endOfProduction, Double zeroToAHundred, Integer unitsProduced, String carImage, CarCategory carCategory, EnginePosition enginePosition, Transmission transmission, Manufacturer manufacturer, Engine engine) {
        this.id = id;
        this.modelCar = modelCar;
        this.maxSpeed = maxSpeed;
        this.startOfProduction = startOfProduction;
        this.endOfProduction = endOfProduction;
        this.zeroToAHundred = zeroToAHundred;
        this.unitsProduced = unitsProduced;
        this.carImage = carImage;
        setCarCategory(carCategory);
        setEnginePosition(enginePosition);
        this.transmission = transmission;
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

    public Date getStartOfProduction() {
        return startOfProduction;
    }

    public void setStartOfProduction(Date startOfProduction) {
        this.startOfProduction = startOfProduction;
    }

    public Date getEndOfProduction() {
        return endOfProduction;
    }

    public void setEndOfProduction(Date endOfProduction) {
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
