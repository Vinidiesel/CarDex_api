package com.api.cars.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "db_manufacturers")
public class Manufacturer extends RepresentationModel<Manufacturer> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true, length = 25)
    private String name;
    @Column(nullable = false)
    private Date foundation;
    @Column(nullable = false, length = 20)
    private String headOffice;
    @Column(nullable = false)
    private String founder;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<Car> car = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "engine", cascade = CascadeType.ALL)
    @JoinColumn(name = "manufacturer_id")
    private Set<Engine> engines = new HashSet<>();

    public Manufacturer() {
    }

    public Manufacturer(UUID id, String name, Date foundation, String headOffice, String founder) {
        this.id = id;
        this.name = name;
        this.foundation = foundation;
        this.headOffice = headOffice;
        this.founder = founder;
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

    public Date getFoundation() {
        return foundation;
    }

    public void setFoundation(Date foundation) {
        this.foundation = foundation;
    }

    public String getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(String headOffice) {
        this.headOffice = headOffice;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public Set<Car> getCar() {
        return car;
    }

    public Set<Engine> getEngines() {
        return engines;
    }

}
