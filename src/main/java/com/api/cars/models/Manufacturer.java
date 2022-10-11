package com.api.cars.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate foundation;
    @Column(nullable = false, length = 25)
    private String headOffice;
    @Column(nullable = false)
    private String founderName;
    @Column
    private String logo;

    @JsonIgnore
    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.REMOVE)
    private final Set<Car> car = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.REMOVE)
    private final Set<Engine> engines = new HashSet<>();

    public Manufacturer() {
    }

    public Manufacturer(UUID id, String name, LocalDate foundation, String headOffice, String founderName, String logo) {
        this.id = id;
        this.name = name;
        this.foundation = foundation;
        this.headOffice = headOffice;
        this.founderName = founderName;
        this.logo = logo;
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

    public LocalDate getFoundation() {
        return foundation;
    }

    public void setFoundation(LocalDate foundation) {
        this.foundation = foundation;
    }

    public String getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(String headOffice) {
        this.headOffice = headOffice;
    }

    public String getFounderName() {
        return founderName;
    }

    public void setFounderName(String founder) {
        this.founderName = founder;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Car> getCar() {
        return car;
    }

    public Set<Engine> getEngines() {
        return engines;
    }

}
