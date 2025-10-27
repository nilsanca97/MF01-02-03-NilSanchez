package dev.app.rentingCar_boot;

import dev.app.rentingCar_boot.utils.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PopulateTests {

    @Autowired
    PopulateBooking populateBooking;

    @Autowired
    PopulateCar populateCar;

    @Autowired
    PopulateDrivingCourse populateDrivingCourse;

    @Autowired
    PopulateClient populateClient;

    @Autowired
    PopulateAllTables populateAllTables;

    @Test
    void populateAllTables () {

        // let s populate cars first
        PopulateStatus populateCarStatus = populateCar.populateCar(10);
        System.out.println("\nPopulate Car operations: " + populateCarStatus.getQty() +
                " \n" + populateCarStatus.getMessage());

        // let s populate clients
        PopulateStatus populateClientStatus = populateClient.populateClient(10);
        System.out.println("\nPopulate Client operations: " + populateClientStatus.getQty() +
                " \n" + populateClientStatus.getMessage());

        // once cars are populated, let s populate bookings
        PopulateStatus populateBookingStatus = populateBooking.populateBooking(10);
        System.out.println("\nPopulate Booking operations: " + populateBookingStatus.getQty() +
                " \n" + populateBookingStatus.getMessage());

        // once bookings are populated, let s populate driving courses
        PopulateStatus populateDrivingCourseStatus = populateDrivingCourse.populateDrivingCourse(10);
        System.out.println("\nPopulate DrivingCourse operations: " + populateDrivingCourseStatus.getQty() +
                " \n" + populateDrivingCourseStatus.getMessage());
    }

    @Test
    void populateAllTablesAtOnce () {

        String result = populateAllTables.populateAllTables(1000);
        System.out.println(result);
    }



}
