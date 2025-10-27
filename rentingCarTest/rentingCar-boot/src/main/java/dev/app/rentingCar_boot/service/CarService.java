package dev.app.rentingCar_boot.service;

import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarService {

    // let s implement all CRUD operations

    @Autowired
    CarRepository carRepository;

    public Iterable<Car> findAll() {

        Iterable<Car> foundCard =  new ArrayList<>();

        foundCard = carRepository.findAll();

        return foundCard;
    }

    public Optional<Car> findCarById (String id) {

        Optional<Car> foundCar = carRepository.findById(id);

        if (foundCar.isEmpty()) System.out.println("Car not found");
        else System.out.println("Car found: " + foundCar.get());


        return foundCar;
    }

    public void deleteCarById (String id) {

        carRepository.deleteById(id);

        Optional<Car> foundCar = carRepository.findById(id);

        if (foundCar.isEmpty()) System.out.println("Car not found: we can not delete the car");
        else System.out.println("Car found and deleted: " + foundCar);

    }

    public void deleteAllCard (){

        carRepository.deleteAll();
    }

    public void updateCar (Car car){

        carRepository.save(car);
    }




}
