package dev.app.rentingCar_boot.service;

import dev.app.rentingCar_boot.model.Booking;
import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Iterable<Booking> findAll() {
        Iterable<Booking> foundBookings = new ArrayList<>();
        foundBookings = bookingRepository.findAll();
        return foundBookings;
    }

    public Optional<Booking> findBookingById(String id) {
        Optional<Booking> foundBooking = bookingRepository.findById(id);

        if (foundBooking.isEmpty()) {
            System.out.println("Booking not found");
        } else {
            System.out.println("Booking found: " + foundBooking.get());
        }

        return foundBooking;
    }

    public void deleteBookingById(String id) {
        bookingRepository.deleteById(id);

        Optional<Booking> foundBooking = bookingRepository.findById(id);

        if (foundBooking.isEmpty()) {
            System.out.println("Booking not found: we can not delete the booking");
        } else {
            System.out.println("Booking found and deleted: " + foundBooking);
        }
    }

    public void deleteAllBookings() {
        bookingRepository.deleteAll();
    }

    public void updateBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }


}
