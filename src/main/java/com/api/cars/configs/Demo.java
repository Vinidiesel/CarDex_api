package com.api.cars.configs;

import com.api.cars.models.Car;
import com.api.cars.models.Engine;
import com.api.cars.models.Manufacturer;
import com.api.cars.models.enums.CarCategory;
import com.api.cars.models.enums.EnginePosition;
import com.api.cars.models.enums.Fuel;
import com.api.cars.models.enums.Traction;
import com.api.cars.repositories.CarRepository;
import com.api.cars.repositories.EngineRepository;
import com.api.cars.repositories.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class Demo implements CommandLineRunner {

    private final ManufacturerRepository manufacturerRepository;
    private final EngineRepository engineRepository;
    private final CarRepository carRepository;

    @Autowired
    public Demo(ManufacturerRepository manufacturerRepository, EngineRepository engineRepository, CarRepository carRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.engineRepository = engineRepository;
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Manufacturer manufacturer = new Manufacturer(null, "Ferrari", LocalDate.parse("1929-11-16"), "Maranello - Italy", "Enzo Ferrari", "");

        Engine engine = new Engine(null, "Ferrari F136 F", "V8", 9000, "Naturally aspirated", 562, 4.5, Fuel.GASOLINE,manufacturer);

        Car car = new Car(null, "458 Italia Coupe", 202.00, LocalDate.parse("2009-07-28"), LocalDate.parse("2015-03-05"), 3.1, 24649, "", "7 Automatic", Traction.RWD, CarCategory.SPORT_CAR, EnginePosition.CENTRAL, manufacturer, engine);

        manufacturerRepository.save(manufacturer);
        engineRepository.save(engine);
        carRepository.save(car);

    }
}
