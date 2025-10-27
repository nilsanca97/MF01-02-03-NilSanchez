package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.Booking;
import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.Client;
import dev.app.rentingCar_boot.repository.BookingRepository;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.ClientRepository;
import dev.app.rentingCar_boot.utils.PopulateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PopulateBooking {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ClientRepository clientRepository;

    public PopulateStatus populateBooking(int qty) {
        StringBuilder messageBuilder = new StringBuilder();
        boolean[] operationResults = new boolean[2];
        int operationIndex = 0;
        
        try {
            // Operation 1: Generate Bookings
            List<Booking> bookings = generateBookings(qty);
            operationResults[operationIndex] = bookings != null && bookings.size() == qty;
            messageBuilder.append(" Operation 1: Generated ").append(bookings != null ? bookings.size() : 0)
                         .append(" bookings (requested: ").append(qty).append(")\n");
            operationIndex++;
            
            // Operation 2: Assign Cars and Clients to Bookings
            assignCarsAndClientsToBookings(bookings);
            operationResults[operationIndex] = true; // Assume success if no exception
            messageBuilder.append(" Operation 2: Assigned cars and clients to bookings successfully\n");
            
        } catch (Exception e) {
            // Mark current and remaining operations as failed
            for (int i = operationIndex; i < 2; i++) {
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
        
        return new PopulateStatus(allSuccess, messageBuilder.toString().trim(), qty);
    }

    public List<Booking> generateBookings(int qtyBookings) {
        List<Booking> generatedBookings = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < qtyBookings; i++) {
            String id = "B" + String.format("%03d", i + 1);
            
            // Generate random booking date (within last 2 years or next 6 months)
            LocalDate startDate = LocalDate.now().minusYears(2);
            LocalDate endDate = LocalDate.now().plusMonths(6);
            long daysBetween = endDate.toEpochDay() - startDate.toEpochDay();
            LocalDate randomDate = startDate.plusDays(random.nextLong(daysBetween));
            int bookingDate = (int) randomDate.toEpochSecond(LocalDate.now().atStartOfDay().toLocalTime(), ZoneOffset.UTC);
            
            // Generate random rental duration (1-30 days)
            int qtyDays = 1 + random.nextInt(30);
            
            // Generate random total amount based on days (50-200 per day)
            double dailyRate = 50.0 + (random.nextDouble() * 150.0);
            double totalAmount = Math.round((dailyRate * qtyDays) * 100.0) / 100.0;
            
            // Random active status (80% chance of being active)
            boolean isActive = random.nextDouble() < 0.8;

            Booking booking = new Booking(bookingDate, qtyDays, totalAmount, isActive, null, null);
            generatedBookings.add(booking);
            bookingRepository.save(booking);
        }

        return generatedBookings;
    }

    public void assignCarsAndClientsToBookings(List<Booking> bookings) {
        Random random = new Random();
        
        // Get all available cars and clients from database
        List<Car> availableCars = (List<Car>) carRepository.findAll();
        List<Client> availableClients = (List<Client>) clientRepository.findAll();
        
        /*if (availableCars.isEmpty()) {
            throw new RuntimeException("No cars available in database. Please populate cars first.");
        }
        
        if (availableClients.isEmpty()) {
            throw new RuntimeException("No clients available in database. Please populate clients first.");
        }*/

        for (Booking booking : bookings) {
            // Assign random car
            Car randomCar = availableCars.get(random.nextInt(availableCars.size()));
            booking.setCar(randomCar);
            
            // Assign random client
            Client randomClient = availableClients.get(random.nextInt(availableClients.size()));
            booking.setClient(randomClient);
            
            bookingRepository.save(booking);
        }
    }


    //------------------------------------- UTILS ----------------------------------------------

    /*public List<Booking> generateBookingsForSpecificCar(String carId, int qtyBookings) {
        List<Booking> generatedBookings = new ArrayList<>();
        Random random = new Random();
        
        Car car = carRepository.findById(carId).orElse(null);
        if (car == null) {
            throw new RuntimeException("Car with ID " + carId + " not found.");
        }
        
        List<Client> availableClients = (List<Client>) clientRepository.findAll();
        if (availableClients.isEmpty()) {
            throw new RuntimeException("No clients available in database. Please populate clients first.");
        }

        for (int i = 0; i < qtyBookings; i++) {
            String id = "B" + carId + String.format("%02d", i + 1);
            
            // Generate booking date within last year
            LocalDate startDate = LocalDate.now().minusYears(1);
            LocalDate endDate = LocalDate.now();
            long daysBetween = endDate.toEpochDay() - startDate.toEpochDay();
            LocalDate randomDate = startDate.plusDays(random.nextLong(daysBetween));
            int bookingDate = (int) randomDate.toEpochSecond(LocalDate.now().atStartOfDay().toLocalTime(), ZoneOffset.UTC);
            
            int qtyDays = 1 + random.nextInt(14); // 1-14 days for specific car
            double totalAmount = Math.round((car.getPrice() * qtyDays) * 100.0) / 100.0;
            boolean isActive = random.nextDouble() < 0.9; // 90% active for specific car bookings
            
            Client randomClient = availableClients.get(random.nextInt(availableClients.size()));

            Booking booking = new Booking(id, bookingDate, qtyDays, totalAmount, isActive, car, randomClient);
            generatedBookings.add(booking);
            bookingRepository.save(booking);
        }

        return generatedBookings;
    }

    public List<Booking> generateBookingsForSpecificClient(String clientId, int qtyBookings) {
        List<Booking> generatedBookings = new ArrayList<>();
        Random random = new Random();
        
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new RuntimeException("Client with ID " + clientId + " not found.");
        }
        
        List<Car> availableCars = (List<Car>) carRepository.findAll();
        if (availableCars.isEmpty()) {
            throw new RuntimeException("No cars available in database. Please populate cars first.");
        }

        for (int i = 0; i < qtyBookings; i++) {
            String id = "B" + clientId + String.format("%02d", i + 1);
            
            // Generate booking date within last 6 months
            LocalDate startDate = LocalDate.now().minusMonths(6);
            LocalDate endDate = LocalDate.now();
            long daysBetween = endDate.toEpochDay() - startDate.toEpochDay();
            LocalDate randomDate = startDate.plusDays(random.nextLong(daysBetween));
            int bookingDate = (int) randomDate.toEpochSecond(LocalDate.now().atStartOfDay().toLocalTime(), ZoneOffset.UTC);
            
            int qtyDays = 1 + random.nextInt(21); // 1-21 days for specific client
            Car randomCar = availableCars.get(random.nextInt(availableCars.size()));
            double totalAmount = Math.round((randomCar.getPrice() * qtyDays) * 100.0) / 100.0;
            boolean isActive = true; // All bookings for specific client are active

            Booking booking = new Booking(id, bookingDate, qtyDays, totalAmount, isActive, randomCar, client);
            generatedBookings.add(booking);
            bookingRepository.save(booking);
        }

        return generatedBookings;
    }

    public void generateBookingsBetweenDates(LocalDate startDate, LocalDate endDate, int qtyBookings) {
        Random random = new Random();
        
        List<Car> availableCars = (List<Car>) carRepository.findAll();
        List<Client> availableClients = (List<Client>) clientRepository.findAll();
        
        if (availableCars.isEmpty() || availableClients.isEmpty()) {
            throw new RuntimeException("Insufficient cars or clients in database.");
        }

        long daysBetween = endDate.toEpochDay() - startDate.toEpochDay();
        
        for (int i = 0; i < qtyBookings; i++) {
            String id = "BD" + String.format("%04d", i + 1);
            
            LocalDate randomDate = startDate.plusDays(random.nextLong(daysBetween));
            int bookingDate = (int) randomDate.toEpochSecond(LocalDate.now().atStartOfDay().toLocalTime(), ZoneOffset.UTC);
            
            int qtyDays = 1 + random.nextInt(10);
            Car randomCar = availableCars.get(random.nextInt(availableCars.size()));
            Client randomClient = availableClients.get(random.nextInt(availableClients.size()));
            double totalAmount = Math.round((randomCar.getPrice() * qtyDays) * 100.0) / 100.0;
            boolean isActive = random.nextDouble() < 0.85;

            Booking booking = new Booking(id, bookingDate, qtyDays, totalAmount, isActive, randomCar, randomClient);
            bookingRepository.save(booking);
        }
    }*/
}
