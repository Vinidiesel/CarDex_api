package com.api.cars.models;

import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "db_images")
public class Images extends RepresentationModel<Car> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String fileName;
    @Column
    private Byte[] data;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;
    @ManyToOne
    @JoinColumn(name = "engine_id")
    private Engine engine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
