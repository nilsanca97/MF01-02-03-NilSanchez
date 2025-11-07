package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CarRepository extends JpaRepository<Car, String> {
    List<Car> findByBrand(String brand);
}