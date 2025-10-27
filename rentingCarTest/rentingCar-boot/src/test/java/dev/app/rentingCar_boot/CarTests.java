package dev.app.rentingCar_boot;

import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.CarExtras;
import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.repository.CarExtrasRepository;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.InssuranceCiaRepository;
import dev.app.rentingCar_boot.service.CarService;
import dev.app.rentingCar_boot.utils.PopulateCar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import dev.app.rentingCar_boot.service.GenerateBooking;

@SpringBootTest
class RentingCarBootApplicationTests {

    @Autowired
    CarRepository carRepository;

    @Autowired
    CarService carService;

    @Autowired
    CarExtrasRepository carExtrasRepository;

    @Autowired
    InssuranceCiaRepository inssuranceCiaRepository;

    @Autowired
    PopulateCar populateCar;

    @Autowired
    GenerateBooking generateBooking;

	@Test
	void contextLoads() {
	}


    @Test
    void testCarRepository() {

        //Car car = new Car("1", "Toyota", "Corolla", "123456", 2022, 100.0);
        //carRepository.save(car);

        // Car car2 = new Car("2", "Toyota", "Corolla", "123456", 2022, 100.0);
        // carRepository.save(car2);

        // Car car3 = new Car("3", "Toyota", "Corolla", "123456", 2022, 100.0);
        // carRepository.save(car3);

        // Car car4 = new Car("4", "Toyota", "Corolla", "123456", 2022, 100.0);
        // carRepository.save(car4);

        //Car car5 = new Car("5", "Toyota", "Corolla", "123456", 2022, 100.0);
        //carRepository.save(car5);

    }


    @Test
    void tesFindCarById() {
        String id = "10";
        Optional<Car> car = carService.findCarById(id);
    }


    @Test
    void testDeleteById(){

        String id = "1";
        carService.deleteCarById(id);
    }

    @Test
    void testAssignCarToCarExtras(){
    //void testAssignCarExtraToCar(){

        CarExtras myCarExtras = new CarExtras(
                "GPS", "High precission GPS",
                50.0, true, "ELECTRONIC"  );
        carExtrasRepository.save(myCarExtras);
        System.out.println("CarExtras -object-: " + carExtrasRepository.findById("1").get());

        System.out.println("CarExtras --from db-: " + myCarExtras);
        Optional<Car> myCar = carService.findCarById("6157");
        System.out.println("Car: " + myCar.get());

        //if (myCar.isEmpty()) System.out.println("Car not found");
        //else myCar.get().getCarExtras().add(myCarExtras);
        //addCarExtra(myCarExtras);

        // we assign myCarExtras to myCar and save it
        myCarExtras.setCarFK(myCar.get());
        carExtrasRepository.save(myCarExtras);

    }

    @Test
    void testAssignCarExtraToCarAndCarToInsurance() {

        // todo
        // create a car extras: myCarExtras
        CarExtras myCarExtras = new CarExtras(
                 "GPS", "High precission GPS",
                50.0, true, "ELECTRONIC"  );
        carExtrasRepository.save(myCarExtras);
        System.out.println("CarExtras -object-: " + carExtrasRepository.findById("1").get());

        System.out.println("CarExtras --from db-: " + myCarExtras);

        // create an insurance cia: myInssuranceCia
        InssuranceCia myInssuranceCia = new InssuranceCia();
        myInssuranceCia.setId("1");
        myInssuranceCia.setName("alliance");
        myInssuranceCia.setActive(true);
        myInssuranceCia.setQtyEmployee(1000);
        inssuranceCiaRepository.save(myInssuranceCia);
        System.out.println("InssuranceCia -object-: " + inssuranceCiaRepository.findById("1").get());

        // fetch a car: id="6157"
        Optional<Car> myOpCar = carService.findCarById("6157");
        System.out.println("Car: " + myOpCar.get());

        // assign to manay side: carExtras to car
        myCarExtras.setCarFK(myOpCar.get());
        carExtrasRepository.save(myCarExtras);
        // save carExtras
        // assign to many side: car to insurance
        myOpCar.get().setInssuranceCia(myInssuranceCia);
        // save car
        carRepository.save(myOpCar.get());

    }


    @Test
    void testPopulateCar() {
        populateCar.populateCar(10);
    }

    @Test
    void testAssignAvailableDatesToCarByYear() {
        List<Car> cars = (List<Car>) carRepository.findAll();
        populateCar.assignAvailableDatesToCarByYear(cars, 2026);
    }

    @Test
    void testUnixTimestampAvailability() {
        // Get first car from repository
        List<Car> cars = (List<Car>) carRepository.findAll();
        assertFalse(cars.isEmpty(), "Should have cars in database");
        Car myCar = cars.get(0);

        System.out.println("Selected car: " + myCar);

        // Clear existing availability data to avoid conflicts from previous test runs
        //myCar.getAvailableDates().clear();
        //carRepository.save(myCar);
        //System.out.println("Selected car after clearing dates: " + myCar);

        // Generate available dates for 2026 using Unix timestamps
        //populateCar.generateAvailableDates(2026, myCar);
        //carRepository.save(myCar);
        //System.out.println("Selected car with Available dates: " + myCar);

        // Test availability check for February 2026 using Unix timestamps
        // Create Unix timestamp for February 1, 2026 at 00:00:00 GMT
        LocalDate feb1_2026 = LocalDate.of(2026, 2, 1);
        long feb1UnixTimestamp = feb1_2026.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        
        // Check availability for all February 2026 (28 days)
        boolean isAvailableFebruary = generateBooking.checkAvailability(myCar, (int) feb1UnixTimestamp, 28);
        
        System.out.println("Unix timestamp for Feb 1, 2026: " + feb1UnixTimestamp);
        System.out.println("Car available for all February 2026: " + isAvailableFebruary);

        // Test specific dates from your table
        // January 1, 2026 should be 1767225600
        LocalDate jan1_2026 = LocalDate.of(2026, 1, 1);
        long jan1UnixTimestamp = jan1_2026.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        System.out.println("Unix timestamp for Jan 1, 2026: " + jan1UnixTimestamp + " (expected: 1767225600)");
        assertEquals(1767225600L, jan1UnixTimestamp, "January 1, 2026 should be 1767225600");
    }

    @Test
    void testUnixTimestampAvailability_Fail() {
        // Get first car from repository
        List<Car> cars = (List<Car>) carRepository.findAll();
        assertFalse(cars.isEmpty(), "Should have cars in database");
        Car myCar = cars.get(0);

        System.out.println("Selected car: " + myCar);

        // Test availability check for December 2026 using Unix timestamps
        // Create Unix timestamp for December 1, 2026 at 00:00:00 GMT
        LocalDate december1_2026 = LocalDate.of(2026, 12, 1);
        long dec1UnixTimestamp = december1_2026.atStartOfDay(ZoneOffset.UTC).toEpochSecond();

        // Check availability for all February 2026 (28 days)
        boolean isAvailableDecember = generateBooking.checkAvailability(myCar, (int) dec1UnixTimestamp, 31);

        System.out.println("Unix timestamp for Feb 1, 2026: " + dec1UnixTimestamp);
        System.out.println("Car available for all February 2026: " + isAvailableDecember);

        // Test specific dates from your table
        // January 1, 2026 should be 1767225600
        LocalDate jan1_2026 = LocalDate.of(2026, 1, 1);
        long jan1UnixTimestamp = jan1_2026.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        System.out.println("Unix timestamp for Jan 1, 2026: " + jan1UnixTimestamp + " (expected: 1767225600)");
        assertEquals(1767225600L, jan1UnixTimestamp, "January 1, 2026 should be 1767225600");
    }


}
