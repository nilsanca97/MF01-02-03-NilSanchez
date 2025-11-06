package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.InssuranceCiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PopulateAllTables {

    @Autowired
    private PopulateCar populateCar;

    @Autowired
    private PopulateClient populateClient;

    @Autowired
    private PopulateBooking populateBooking;

    @Autowired
    private PopulateDrivingCourse populateDrivingCourse;

    @Autowired
    private PopulateInssuranceCia populateInssuranceCia;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private InssuranceCiaRepository inssuranceCiaRepository;

    public String populateAllTables(int qty) {

        //1.1 let s populate cars first
        PopulateStatus populateCarStatus = populateCar.populateCar(qty);
        System.out.println("\nPopulate Car operations: " + populateCarStatus.getQty() +
                " \n" + populateCarStatus.getMessage());
        // 1.2 once cars are populated, lets populate insurance cia
        PopulateStatus populateInssuranceCiaStatus = null;
        if (populateCarStatus.isStatus()) {
            populateInssuranceCiaStatus = populateInssuranceCia.populateInssuranceCia(qty);
            System.out.println("\nPopulate InssuranceCia operations: " + populateInssuranceCiaStatus.getQty() +
                    " \n" + populateInssuranceCiaStatus.getMessage());
        } else return "Populate Car operation failed";

        if (!populateInssuranceCiaStatus.isStatus()) return "Populate InssuranceCia operation failed";

        // 1.3 once inssuranceCias are populated, lets  assign InssuranceCiaToCar
        populateCar.assignInssuranceCiaToCar(carRepository.findAll(), inssuranceCiaRepository.findAll());

        // let s populate clients
        PopulateStatus populateClientStatus = null;
        if (populateCarStatus.isStatus()) {
        populateClientStatus = populateClient.populateClient(qty);
        System.out.println("\nPopulate Client operations: " + populateClientStatus.getQty() +
                " \n" + populateClientStatus.getMessage());
        } else return "Populate InssuranceCia operations failed";


        // once cars are populated, let s populate bookings
        PopulateStatus populateBookingStatus = null;
        if (populateClientStatus.isStatus()) {
        populateBookingStatus = populateBooking.populateBooking(qty);
        System.out.println("\nPopulate Booking operations: " + populateBookingStatus.getQty() +
                " \n" + populateBookingStatus.getMessage());
        } else return "Populate Client operations failed";

        // once bookings are populated, let s populate driving courses
        PopulateStatus populateDrivingCourseStatus = null;
        if (populateBookingStatus.isStatus()){
        populateDrivingCourseStatus = populateDrivingCourse.populateDrivingCourse(qty);
        System.out.println("\nPopulate DrivingCourse operations: " + populateDrivingCourseStatus.getQty() +
                " \n" + populateDrivingCourseStatus.getMessage());
        } else return "Populate Booking operations failed";

        if (!populateDrivingCourseStatus.isStatus()) return "Populate DrivingCourse operations failed";

    return "Populate All Tables operations completed successfully";
    }

}
