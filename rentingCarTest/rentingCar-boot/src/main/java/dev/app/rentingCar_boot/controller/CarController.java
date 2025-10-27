package dev.app.rentingCar_boot.controller;


import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class CarController {

    @Autowired
    CarRepository carRepository;

    @GetMapping("/cars-nocss-data/")
    public String listCarsNoCssData(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        //System.out.println("Cars (controller/cars-nocss): " + carRepository.findAll());
        return "cars-nocss-data";
    }

    @GetMapping("/cars-nocss")
    public String listCarsNoCss(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        //System.out.println("Cars (controller/cars-nocss): " + carRepository.findAll());
        return "cars-nocss";
    }

    @GetMapping("/cars")
    public String listCars(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        //System.out.println("Cars (controller/cars): " + carRepository.findAll());
        return "cars";
    }

    @GetMapping("/cars-relation")
    public String listCarsRelated(Model model) {
        Iterable<Car> carsIterable = carRepository.findAll();
        List<Car> cars = new ArrayList<>();
        carsIterable.forEach(cars::add);
        
        model.addAttribute("cars", cars);
        model.addAttribute("rentalCompany", "Premium Car Rental");
        model.addAttribute("totalCars", cars.size());
        model.addAttribute("fleetStatus", "Active Fleet with Relations");
        //System.out.println("Cars with relations (controller/cars-relation): " + cars);
        return "cars-relation";
    }

    /*@GetMapping("/cars/generate")
    public String generateCars(@RequestParam int qtyCars) {
        List<Car> generatedCars = new ArrayList<>();
        Random random = new Random();
        
        String[] brands = {"Toyota", "Honda", "Ford", "BMW", "Mercedes", "Audi", "Volkswagen", "Nissan", "Hyundai", "Kia"};
        String[] models = {"Sedan", "SUV", "Hatchback", "Coupe", "Convertible", "Wagon", "Pickup", "Crossover"};
        
        for (int i = 0; i < qtyCars; i++) {
            String brand = brands[random.nextInt(brands.length)];
            String model = models[random.nextInt(models.length)];
            String plate = generateRandomPlate(random);
            int year = 2010 + random.nextInt(15); // Years between 2010-2024
            double price = 50.0 + (random.nextDouble() * 450.0); // Price between 50-500
            
            Car car = new Car(brand, model, plate, year, price);
            generatedCars.add(car);
            carRepository.save(car);
        }
        
        //return "Successfully generated " + qtyCars + " cars. Total cars in database: " + carRepository.count();
        return "cars";
    }
    
    private String generateRandomPlate(Random random) {
        StringBuilder plate = new StringBuilder();
        // Generate 3 letters
        for (int i = 0; i < 3; i++) {
            plate.append((char) ('A' + random.nextInt(26)));
        }
        // Generate 4 numbers
        for (int i = 0; i < 4; i++) {
            plate.append(random.nextInt(10));
        }
        return plate.toString();
    }*/

}
