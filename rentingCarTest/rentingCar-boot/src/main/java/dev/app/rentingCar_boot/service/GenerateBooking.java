package dev.app.rentingCar_boot.service;

import dev.app.rentingCar_boot.model.Booking;
import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.Client;
import dev.app.rentingCar_boot.repository.BookingRepository;
import dev.app.rentingCar_boot.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;

@Service
public class GenerateBooking {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private CarRepository carRepository;

    public String generateBooking(Client client, Car car, int bookingDate, int qtyDays) {
        StringBuilder result = new StringBuilder();

        // 1. defensive programming : not null data
        if (client == null) {
            return "Error: Client cannot be null";
        }
        if (car == null) {
            return "Error: Car cannot be null";
        }
        if (qtyDays <= 0) {
            return "Error: Quantity of days must be greater than 0";
        }
        if (bookingDate <= 0) {
            return "Error: Booking date must be a valid Unix timestamp";
        }

        // 2. check availability
        if (!checkAvailability(car, bookingDate, qtyDays)) {
            return "Error: Car is not available for the requested dates";
        }

        // 3. calculate total amount: maths at calculateTotalAmount
        double totalAmount = calculateTotalAmount(car, qtyDays);

        // 4. create booking: Booking myBooking = new Booking();
        Booking myBooking = new Booking();

        // 5. setting our booking with revised and legit data
        myBooking.setBookingDate(bookingDate);
        myBooking.setQtyDays(qtyDays);
        myBooking.setTotalAmount(totalAmount);
        myBooking.setActive(true);
        myBooking.setCar(car);
        myBooking.setClient(client);

        // 6. call createBooking and save it with bookingRepository
        Booking savedBooking = bookingRepository.save(myBooking);

        // 7. update Car availability attribute
        updateCarAvailability(car, bookingDate, qtyDays);
        carRepository.save(car);

        // 8 return booking done with string builder with all relevant data
        result.append("Booking successfully created!\n");
        result.append("Booking ID: ").append(savedBooking.getId()).append("\n");
        result.append("Client: ").append(client.getName()).append(" ").append(client.getLastName()).append("\n");
        result.append("Car: ").append(car.getBrand()).append(" ").append(car.getModel()).append(" (").append(car.getPlate()).append(")\n");
        result.append("Start Date: ").append(formatUnixTimestamp(bookingDate)).append("\n");
        result.append("Duration: ").append(qtyDays).append(" days\n");
        result.append("Total Amount: €").append(String.format("%.2f", totalAmount)).append("\n");
        result.append("Status: Active");

        return result.toString();
    }

    /**
     * Calculate total amount for the booking
     * @param car
     * @param qtyDays
     * @return
     */
    public double calculateTotalAmount(Car car, int qtyDays) {
        // Simple calculation: quantity of days * price per day
        return qtyDays * car.getPrice();
    }

    /**
     * Check if the car is available for the requested booking period
     * @param car
     * @param bookingDate
     * @param qtyDays
     * @return
     */
    public boolean checkAvailability(Car car, int bookingDate, int qtyDays) {
        Map<Integer, Boolean> availableDates = car.getAvailableDates();

        // Check each day in the requested booking period
        for (int i = 0; i < qtyDays; i++) {
            int currentDate = bookingDate + (i * 86400); // Add 86400 seconds (1 day) per iteration

            // If date exists in HashMap and is false (unavailable), return false
            if (availableDates.containsKey(currentDate) && !availableDates.get(currentDate)) {
                return false; // Car is not available on this date
            }
        }

        return true; // All dates are available
    }

    /**
     * Updates car availability by marking booked dates as unavailable
     */
    private void updateCarAvailability(Car car, int bookingDate, int qtyDays) {
        Map<Integer, Boolean> availableDates = car.getAvailableDates();
        
        // Mark each day in the booking period as unavailable (false)
        for (int i = 0; i < qtyDays; i++) {
            int currentDate = bookingDate + (i * 86400); // Add 86400 seconds (1 day) per iteration
            availableDates.put(currentDate, false); // Mark as unavailable
        }
    }

    /**
     * Formats Unix timestamp to readable date format
     */
    private String formatUnixTimestamp(int timestamp) {
        LocalDate date = Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.UTC).toLocalDate();
        return String.format("%02d/%02d/%d", 
            date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }
}
