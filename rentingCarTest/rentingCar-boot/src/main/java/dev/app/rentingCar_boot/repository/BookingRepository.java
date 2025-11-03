package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.Booking;
import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, String> {

    // Existing query
    List<Booking> findByBookingDateLessThan(int bookingDate);

    // 1. Find all active bookings
    List<Booking> findByIsActiveTrue();

    // 2. Find all inactive/cancelled bookings
    List<Booking> findByIsActiveFalse();

    // 3. Find bookings by car
    List<Booking> findByCar(Car car);

    // 4. Find bookings by client
    List<Booking> findByClient(Client client);

    // 5. Find bookings within a date range
    List<Booking> findByBookingDateBetween(int startDate, int endDate);

    // 6. Find active bookings for a specific car
    List<Booking> findByCarAndIsActiveTrue(Car car);

    // 7. Find bookings by total amount greater than specified value
    List<Booking> findByTotalAmountGreaterThan(double amount);

    // 8. Find bookings by quantity of days
    List<Booking> findByQtyDays(int qtyDays);

    // 9. Custom query to check for overlapping bookings for a car within date range
    @Query("SELECT b FROM Booking b WHERE b.car = :car AND b.isActive = true AND " +
            "((b.bookingDate <= :endDate) AND (b.bookingDate + (b.qtyDays * 86400) >= :startDate))")
    List<Booking> findOverlappingBookingsForCar(@Param("car") Car car,
                                                @Param("startDate") int startDate,
                                                @Param("endDate") int endDate);

    // 10. Find top bookings by total amount (most expensive first)
    List<Booking> findTop10ByOrderByTotalAmountDesc();
}
