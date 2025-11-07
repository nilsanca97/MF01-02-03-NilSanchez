package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, String> {
    Iterable<Car> findAll();
    Optional<Car> findCarById(String id);
    List<Car> findByYear (int year);
    List<Car> findByBrand(String brand);

    Optional<Car> deleteCarById (String id);
    Optional<Car> updateCar (Car car);
}