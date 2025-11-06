package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.model.InsuranceContract;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.InssuranceCiaRepository;
import dev.app.rentingCar_boot.repository.InsuranceContractRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    @Autowired
    private InsuranceContractRepository insuranceContractRepository;

    public String populateAllTables(int qty) {

        //1.1 let s populate cars first
        PopulateStatus populateCarStatus = populateCar.populateCar(qty);
        System.out.println("\nPopulate Car operations: " + populateCarStatus.getQty() +
                " \n" + populateCarStatus.getMessage());
        // 1.2 once cars are populated, lets populate insurance companies
        PopulateStatus populateInssuranceCiaStatus = null;
        if (populateCarStatus.isStatus()) {
            populateInssuranceCiaStatus = populateInssuranceCia.populateInssuranceCia(qty);
            System.out.println("\nPopulate InssuranceCia operations: " + populateInssuranceCiaStatus.getQty() +
                    " \n" + populateInssuranceCiaStatus.getMessage());
        } else return "Populate Car operation failed";

        if (!populateInssuranceCiaStatus.isStatus()) return "Populate InssuranceCia operation failed";

        /*// 1.3 once inssuranceCias are populated, lets  assign InssuranceCiaToCar
        populateCar.assignInssuranceCiaToCar(carRepository.findAll(), inssuranceCiaRepository.findAll());
        */
        // 1.3 once inssuranceCias are populated, lets assign InssuranceCiaToCar
        List<Car> allCars = (List<Car>) carRepository.findAll();
        List<InssuranceCia> allInssuranceCias = (List<InssuranceCia>) inssuranceCiaRepository.findAll();

        //populateCar.assignInssuranceCiaToCar(allCars, allInssuranceCias);
        assignInssuranceCiaToCar(allCars, allInssuranceCias);
        System.out.println("\nAssigned insurance companies to cars successfully via InsuranceContract bridge.");

        // 2 Populate clients
        PopulateStatus populateClientStatus = null;
        if (populateCarStatus.isStatus()) {
        populateClientStatus = populateClient.populateClient(qty);
        System.out.println("\nPopulate Client operations: " + populateClientStatus.getQty() +
                " \n" + populateClientStatus.getMessage());
        } else return "Populate InssuranceCia operations failed";


        // once cars are populated,
        // 3. let s populate bookings
        PopulateStatus populateBookingStatus = null;
        if (populateClientStatus.isStatus()) {
        populateBookingStatus = populateBooking.populateBooking(qty);
        System.out.println("\nPopulate Booking operations: " + populateBookingStatus.getQty() +
                " \n" + populateBookingStatus.getMessage());
        } else return "Populate Client operations failed";

        // once bookings are populated,
        // 4. let s populate driving courses
        PopulateStatus populateDrivingCourseStatus = null;
        if (populateBookingStatus.isStatus()){
        populateDrivingCourseStatus = populateDrivingCourse.populateDrivingCourse(qty);
        System.out.println("\nPopulate DrivingCourse operations: " + populateDrivingCourseStatus.getQty() +
                " \n" + populateDrivingCourseStatus.getMessage());
        } else return "Populate Booking operations failed";

        if (!populateDrivingCourseStatus.isStatus()) return "Populate DrivingCourse operations failed";

    return "Populate All Tables operations completed successfully";
    }

    // Assign a random inssuranceCia to each car,
    // by creating a bridge contract(insuranceContract) between car and inssuranceCia
    @Transactional
    public void assignInssuranceCiaToCar(List<Car> cars, List<InssuranceCia> inssuranceCias) {
        // defensive programming
        System.out.println("\n--- Starting assignInssuranceCiaToCar ---");

        if (cars == null || cars.isEmpty()) {
            System.out.println("No cars available to assign insurance companies.");
            return;
        }
        if (inssuranceCias == null || inssuranceCias.isEmpty()) {
            System.out.println("No insurance companies available to assign to cars.");
            return;
        }
        //finish with defensive programming

        // start with create method "assignInssuranceCiaToCar"
        Random random = new Random();
        int assignedCount = 0;

        for (Car car : cars) {
            // 1.select/ choose a random inssuranceCia
            InssuranceCia inssuranceCia = inssuranceCias.get(random.nextInt(inssuranceCias.size()));

            //2. create InsuranceContract between car-InssuranceCia
            InsuranceContract contract = new InsuranceContract();
            contract.setContractId(UUID.randomUUID().toString());
            contract.setCar(car);
            contract.setInssuranceCia(inssuranceCia);
            contract.setStartDate(LocalDate.now());
            contract.setEndDate(LocalDate.now().plusYears(1));

            //3.Keep bidirectional relationship (opcional, but recommended)
            car.getInsuranceContracts().add(contract);
            inssuranceCia.getInsuranceContracts().add(contract);

            //4.Save insuranceContract
            insuranceContractRepository.save(contract);
            assignedCount++;

            // Log for debug
            System.out.println("Assigned insurance " + inssuranceCia.getName() +
                                " to car " + car.getBrand() +
                                " (contract id: " + contract.getContractId() + ")");
        }

        System.out.println("Finished assignInssuranceCiaToCar");
        System.out.println("Total contracts created: " + assignedCount);
    }

}
