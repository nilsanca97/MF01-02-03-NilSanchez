package dev.app.rentingCar_boot;

import dev.app.rentingCar_boot.model.Booking;
import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.Client;
import dev.app.rentingCar_boot.repository.BookingRepository;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.ClientRepository;
import dev.app.rentingCar_boot.utils.PopulateAllTables;
import dev.app.rentingCar_boot.utils.PopulateBooking;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@Transactional
@SpringBootTest
public class BookingTests {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    PopulateBooking populateBooking;

    @Autowired
    PopulateAllTables populateAllTables;

    @Test
    void bookingTest(){

        Client client = new Client(
                "Nil", "Gasol"
                , "nil.gasol@gmail.com",
                false, 25, "1234");

        clientRepository.save(client);
        System.out.println("Client --from DB--: " + clientRepository.findById("00024").get());

        Car car = carRepository.findById("5225").get();

        Booking myBooking = new Booking();
        myBooking.setId("B019");
        myBooking.setBookingDate(1760344181);
        myBooking.setQtyDays(5);
        myBooking.setTotalAmount(100.26);
        myBooking.setActive(true);

        myBooking.setClient(client);
        myBooking.setCar(car);

        bookingRepository.save(myBooking);

        System.out.println("Booking: " + myBooking);

        System.out.println("Booking --from db--: " + bookingRepository.findById("B019").get());
        System.out.println("Car --from db--: " + carRepository.findById("5225").get());

    }

    @Test
    void populateBookingTest(){
        populateBooking.populateBooking(10);
    }

    @Test
    void createBookingAfterPopulateAllTablesTest() {
        //Step 1: Execute PopulateAllTables to ensure we have data
        String populateResult = populateAllTables.populateAllTables(5);
        System.out.println("Populate result: " + populateResult);

        // Verify population was successful
        //assertEquals("Populate All Tables operations completed successfully", populateResult);

        // Step 2: Verify we have cars and clients available
        List<Car> availableCars = (List<Car>) carRepository.findAll();
        List<Client> availableClients = (List<Client>) clientRepository.findAll();

        assertFalse(availableCars.isEmpty(), "Should have cars after population");
        assertFalse(availableClients.isEmpty(), "Should have clients after population");

        System.out.println("Available cars: " + availableCars.size());
        System.out.println("Available clients: " + availableClients.size());

        // Step 3: Create a new booking using populated data
        Car selectedCar = availableCars.get(0); // Get first available car
        Client selectedClient = availableClients.get(0); // Get first available client

        // Create booking with current date as epoch days
        LocalDate bookingStartDate = LocalDate.now().plusDays(1); // Tomorrow
        int bookingDateEpoch = (int) bookingStartDate.toEpochDay();
        int rentalDays = 7;
        double dailyRate = selectedCar.getPrice();
        double totalAmount = dailyRate * rentalDays;

        Booking newBooking = new Booking(
                bookingDateEpoch,
                rentalDays,
                totalAmount,
                true, // isActive
                selectedCar,
                selectedClient
        );

        // Step 4: Save the new booking
        Booking savedBooking = bookingRepository.save(newBooking);

        // Step 5: Verify the booking was created successfully
        assertNotNull(savedBooking);
        assertNotNull(savedBooking.getId());
        assertEquals(bookingDateEpoch, savedBooking.getBookingDate());
        assertEquals(rentalDays, savedBooking.getQtyDays());
        assertEquals(totalAmount, savedBooking.getTotalAmount(), 0.01);
        assertTrue(savedBooking.isActive());
        assertEquals(selectedCar.getId(), savedBooking.getCar().getId());
        assertEquals(selectedClient.getId(), savedBooking.getClient().getId());

        System.out.println("New booking created successfully: " + savedBooking);

        // Step 6: Verify the booking exists in database
        Optional<Booking> retrievedBooking = bookingRepository.findById(savedBooking.getId());
        assertTrue(retrievedBooking.isPresent(), "Booking should exist in database");

        // Step 7: Verify the car now has this booking in its bookings list
        Car updatedCar = carRepository.findById(selectedCar.getId()).get();
        boolean bookingFoundInCar = updatedCar.getBookings().stream()
                .anyMatch(booking -> booking.getId().equals(savedBooking.getId()));
        assertTrue(bookingFoundInCar, "Car should contain the new booking in its bookings list");

        System.out.println("Test completed successfully - Booking created after PopulateAllTables execution");
    }
    


}
