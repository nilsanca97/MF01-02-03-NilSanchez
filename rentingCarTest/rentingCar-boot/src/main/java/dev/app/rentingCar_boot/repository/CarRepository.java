package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, String> {}