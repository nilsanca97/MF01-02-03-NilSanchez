package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.CarExtras;
import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.repository.CarExtrasRepository;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.InssuranceCiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Component
public class PopulateCar {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarExtrasRepository carExtrasRepository;

    @Autowired
    private InssuranceCiaRepository inssuranceCiaRepository;

    public PopulateStatus populateCar(int qty) {
        StringBuilder messageBuilder = new StringBuilder();
        boolean[] operationResults = new boolean[8];
        int operationIndex = 0;
        
        try {
            // Operation 1: Generate Cars (main)
            List<Car> cars = generateCars(qty);
            operationResults[operationIndex] = cars != null && cars.size() == qty;
            messageBuilder.append(" Operation 1: Generated ").append(cars != null ? cars.size() : 0)
                         .append(" cars (requested: ").append(qty).append(")\n");
            operationIndex++;
            
            // Operation 2: Generate CarExtras (main)
            List<CarExtras> carExtrass = generateCarExtras(qty);
            operationResults[operationIndex] = carExtrass != null && carExtrass.size() == qty;
            messageBuilder.append(" Operation 2: Generated ").append(carExtrass != null ? carExtrass.size() : 0)
                         .append(" car extras (requested: ").append(qty).append(")\n");
            operationIndex++;
            
            // Operation 3: Generate InssuranceCias (main)
            List<InssuranceCia> inssuranceCias = generateInssuranceCias(qty);
            operationResults[operationIndex] = inssuranceCias != null && inssuranceCias.size() == qty;
            messageBuilder.append(" Operation 3: Generated ").append(inssuranceCias != null ? inssuranceCias.size() : 0)
                         .append(" insurance companies (requested: ").append(qty).append(")\n");
            operationIndex++;
            
            // Operation 4: Assign CarExtras to Cars
            assignCarToCarExtras(cars, carExtrass);
            operationResults[operationIndex] = true; // Assume success if no exception
            messageBuilder.append(" Operation 4: Assigned car extras to cars successfully\n");
            operationIndex++;
            
            // Operation 5: Assign InssuranceCias to Cars
            assignInssuranceCiaToCar(cars, inssuranceCias);
            operationResults[operationIndex] = true; // Assume success if no exception
            messageBuilder.append(" Operation 5: Assigned insurance companies to cars successfully\n");
            operationIndex++;
            
            // Operation 6: Generate additional Cars (unassigned)
            List<Car> additionalCars = generateCars(10);
            operationResults[operationIndex] = additionalCars != null && additionalCars.size() == 10;
            messageBuilder.append(" Operation 6: Generated ").append(additionalCars != null ? additionalCars.size() : 0)
                         .append(" additional cars (requested: 10)\n");
            operationIndex++;
            
            // Operation 7: Generate additional CarExtras (unassigned)
            List<CarExtras> additionalCarExtras = generateCarExtras(10);
            operationResults[operationIndex] = additionalCarExtras != null && additionalCarExtras.size() == 10;
            messageBuilder.append(" Operation 7: Generated ").append(additionalCarExtras != null ? additionalCarExtras.size() : 0)
                         .append(" additional car extras (requested: 10)\n");
            operationIndex++;
            
            // Operation 8: Generate additional InssuranceCias (unassigned)
            List<InssuranceCia> additionalInssuranceCias = generateInssuranceCias(10);
            operationResults[operationIndex] = additionalInssuranceCias != null && additionalInssuranceCias.size() == 10;
            messageBuilder.append(" Operation 8: Generated ").append(additionalInssuranceCias != null ? additionalInssuranceCias.size() : 0)
                         .append(" additional insurance companies (requested: 10)\n");
            
        } catch (Exception e) {
            // Mark current and remaining operations as failed
            for (int i = operationIndex; i < 8; i++) {
                operationResults[i] = false;
            }
            messageBuilder.append("Error occurred during operation ").append(operationIndex + 1)
                         .append(": ").append(e.getMessage()).append("\n");
        }
        
        // Check if all operations succeeded
        boolean allSuccess = true;
        for (boolean result : operationResults) {
            if (!result) {
                allSuccess = false;
                break;
            }
        }
        
        // Calculate total quantity (main qty + 30 additional entities)
        int totalQty = qty * 3 + 30; // 3 types of entities * qty + 30 additional
        
        return new PopulateStatus(allSuccess, messageBuilder.toString().trim(), totalQty);
    }

    public List<CarExtras> generateCarExtras(int qtyCars) {
        List<CarExtras> generatedCarExtras = new ArrayList<>();
        Random random = new Random();

        String[] extraNames = {"GPS Navigation", "Child Seat", "Roof Rack", "Ski Rack", "Bike Rack", 
                              "WiFi Hotspot", "Dash Cam", "Premium Insurance", "Roadside Assistance", 
                              "Additional Driver", "Fuel Service", "Car Wash"};
        
        String[] categories = {"Navigation", "Safety", "Sport", "Technology", "Insurance", "Service"};
        
        String[] descriptions = {
            "Professional GPS navigation system with real-time traffic updates",
            "Safety-certified child seat for secure transportation",
            "Heavy-duty roof rack for additional cargo capacity",
            "Specialized ski equipment carrier for winter sports",
            "Secure bike mounting system for cycling enthusiasts",
            "High-speed internet connectivity on the go",
            "HD recording device for trip documentation and security",
            "Comprehensive coverage with reduced deductible",
            "24/7 emergency assistance and towing service",
            "Allow additional qualified driver at no extra cost",
            "Pre-filled fuel tank with return flexibility",
            "Professional car cleaning service included"
        };

        for (int i = 0; i < qtyCars; i++) {
            String name = extraNames[random.nextInt(extraNames.length)];
            String description = descriptions[random.nextInt(descriptions.length)];
            String category = categories[random.nextInt(categories.length)];
            //String id = "EXT" + String.format("%04d", i + 1);
            double dailyPrice = 5.0 + (random.nextDouble() * 45.0); // Price between 5-50
            boolean available = random.nextBoolean();

            CarExtras carExtra = new CarExtras(name, description, dailyPrice, available, category);
            generatedCarExtras.add(carExtra);
            carExtrasRepository.save(carExtra);
        }

        return generatedCarExtras;
    }

    public List<Car> generateCars(int qtyCars) {
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
        return generatedCars;
    }

    public List<InssuranceCia> generateInssuranceCias(int qty){
        List<InssuranceCia> generatedInssuranceCias = new ArrayList<>();
        Random random = new Random();

        String[] companyNames = {"State Farm", "Geico", "Progressive", "Allstate", "Liberty Mutual", 
                                "USAA", "Farmers", "Nationwide", "American Family", "Travelers"};
        
        String[] descriptions = {
            "Comprehensive auto insurance with excellent customer service",
            "Affordable car insurance with 24/7 claims support",
            "Innovative insurance solutions with competitive rates",
            "Full coverage auto insurance with roadside assistance",
            "Trusted insurance provider with nationwide coverage",
            "Premium insurance services for military families",
            "Local insurance expertise with personal touch",
            "Reliable coverage with accident forgiveness programs",
            "Family-focused insurance with multi-policy discounts",
            "Professional insurance services with quick claims processing"
        };

        for (int i = 0; i < qty; i++) {
            InssuranceCia inssuranceCia = new InssuranceCia();
            
            //String id = "INS" + String.format("%04d", i + 1);
            String name = companyNames[random.nextInt(companyNames.length)];
            String description = descriptions[random.nextInt(descriptions.length)];
            int qtyEmployee = 50 + random.nextInt(950); // Between 50-1000 employees
            boolean isActive = random.nextBoolean();

            //inssuranceCia.setId(id);
            inssuranceCia.setName(name);
            inssuranceCia.setDescription(description);
            inssuranceCia.setQtyEmployee(qtyEmployee);
            inssuranceCia.setActive(isActive);

            generatedInssuranceCias.add(inssuranceCia);
            inssuranceCiaRepository.save(inssuranceCia);
        }

        return generatedInssuranceCias;
    }

    public static String generateRandomPlate(Random random) {
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
    }

    public void assignCarToCarExtras(List<Car> cars, List<CarExtras> carExtrass) {
        Random random = new Random();

        for (CarExtras carExtras : carExtrass) {
            Car car = cars.get(random.nextInt(cars.size()));
            carExtras.setCarFK(car);
            carExtrasRepository.save(carExtras);
        }
    }

    public void assignInssuranceCiaToCar(List<Car> cars, List<InssuranceCia> inssuranceCias) {
        Random random = new Random();

        for (Car car : cars) {
            InssuranceCia inssuranceCia = inssuranceCias.get(random.nextInt(inssuranceCias.size()));
            car.setInssuranceCia(inssuranceCia);
            carRepository.save(car);
        }
    }

    // ------------------------------ available dates by car and year -------------------

    // Assign available dates to a car for a specific year
    public void assignAvailableDatesToCarByYear(List<Car> cars, int year){
        for (Car car : cars) {
            generateAvailableDates(year, car);
        }
    }

    // Initialize all dates of a year as available using Unix timestamps at 00:00:00 GMT
    public void initAvailableDatesByCarAndYear(int year, Car car) {

        HashMap<Integer, Boolean> availableDates = new HashMap<>();

        // Calculate days in the specified year (handle leap years)
        int daysInYear = isLeapYear(year) ? 366 : 365;

        // Create date keys as Unix timestamps for the specified year
        LocalDate startOfYear = LocalDate.of(year, 1, 1);

        for (int i = 0; i < daysInYear; i++) {
            LocalDate currentDate = startOfYear.plusDays(i);
            // Convert to Unix timestamp at 00:00:00 GMT
            long unixTimestamp = currentDate.atStartOfDay(java.time.ZoneOffset.UTC).toEpochSecond();
            availableDates.put((int) unixTimestamp, true); // All dates initially available
        }

        car.setAvailableDates(availableDates);
        carRepository.save(car);

    }

    // Helper method to check leap years
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // Generate available dates for a car in a specific year using Unix timestamps at 00:00:00 GMT
    public void generateAvailableDates(int year, Car car){
        HashMap<Integer, Boolean> availableDates = new HashMap<>();
        Random random = new Random();

        // First, populate all dates of the year as available (true) using Unix timestamps
        for (int month = 1; month <= 12; month++) {
            int daysInMonth = getDaysInMonth(month, year);
            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = LocalDate.of(year, month, day);
                // Convert to Unix timestamp at 00:00:00 GMT
                long unixTimestamp = date.atStartOfDay(java.time.ZoneOffset.UTC).toEpochSecond();
                availableDates.put((int) unixTimestamp, true);
            }
        }

        // Generate 3 to 6 random unavailable date ranges
        int numberOfRanges = 3 + random.nextInt(4); // 3 to 6 ranges

        for (int i = 0; i < numberOfRanges; i++) {
            // Random range duration: 2 to 8 days
            int rangeDuration = 2 + random.nextInt(7); // 2 to 8 days

            // Random start month and day
            int randomMonth = 1 + random.nextInt(12); // 1 to 12
            int daysInMonth = getDaysInMonth(randomMonth, year);
            int randomDay = 1 + random.nextInt(daysInMonth); // 1 to days in month

            // Make sure the range doesn't exceed the month
            int actualDuration = Math.min(rangeDuration, daysInMonth - randomDay + 1);

            // Mark the range as unavailable (false) using Unix timestamps
            for (int j = 0; j < actualDuration; j++) {
                int currentDay = randomDay + j;
                if (currentDay <= daysInMonth) {
                    LocalDate date = LocalDate.of(year, randomMonth, currentDay);
                    // Convert to Unix timestamp at 00:00:00 GMT
                    long unixTimestamp = date.atStartOfDay(java.time.ZoneOffset.UTC).toEpochSecond();
                    availableDates.put((int) unixTimestamp, false);
                }
            }
        }

        // Set the generated availability data to the car
        car.setAvailableDates(availableDates);
        carRepository.save(car);
    }

    // Helper method to get days in a specific month
    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 2: return isLeapYear(year) ? 29 : 28;
            case 4: case 6: case 9: case 11: return 30;
            default: return 31;
        }
    }


}
