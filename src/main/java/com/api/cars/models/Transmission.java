package com.api.cars.models;

import com.api.cars.models.enums.Traction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_transmission")
public class Transmission extends RepresentationModel<Transmission> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, length = 20)
    private String exchange;

    @Column(nullable = false, length = 4)
    private Integer traction;

    @OneToOne(mappedBy = "transmission", cascade = CascadeType.ALL,
            optional = false)
    private Car car;

    public Transmission(UUID id, String exchange, Traction traction) {
        this.id = id;
        this.exchange = exchange;
        setTraction(traction);
    }

    public Transmission() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

}
