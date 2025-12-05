package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    //Optional <Car> findAll();
    Optional<Car> findCarById(String id);
    List<Car> findByYear (int year);
    List<Car> findByBrand(String brand);

    Optional<Car> deleteCarById (String id);
    //Optional<Car> updateCar (Car car);
}